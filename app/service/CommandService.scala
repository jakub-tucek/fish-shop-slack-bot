package service

import com.google.inject.Inject
import domain._
import javax.inject.Singleton
import play.api.Logger

/**
  * CommandService provides base handling of incoming command. Calls other needed logic based on it's type.
  *
  * @author Jakub Tucek
  */
@Singleton
class CommandService @Inject()(messagePostService: MessagePostService, fishShopClient: FishShopClient, configProvider: ConfigProvider) {
  var state: OrderState = OrderState.empty

  /**
    * Verifies token in command and calls proper logic handler based on command type
    *
    * @param command command
    * @return result of command handling
    */
  def handleCommand(command: InCommand): OutCommand = {
    if (!command.token.equals(configProvider.config.verificationToken)) {
      val msg = "Unauthorized token in received command"
      Logger.error(msg)
      return ErrorOutCommand(msg)
    }
    Logger.debug(s"Command accepted: ${command.command}")

    command.command match {
      case OrderCommand() => handleOrderCommand(command)
      case MenuCommand() => handleOrderMenu(command)
      case ResetOrderCommand() => handleResetOrder(command)
      case CompleteOrderCommand() => handleCompleteOrder(command)
      case StatusCommand() => handleStatusCommand(command)
      case UnknownCommand() => handleUnknownCommand
    }
  }

  private def handleStatusCommand(command: InCommand): OutCommand = {
    messagePostService.postCurrentState(state, command.responseUrl)
    SuccessOutCommand()
  }

  private def handleCompleteOrder(command: InCommand): _root_.domain.OutCommand = {
    if (state.map.isEmpty || state.map.forall { case (_, value) => value.isEmpty }) { // empty map or empty values
      val msg = "No orders are saved"
      Logger.error(msg)
      return ErrorOutCommand(msg)
    }

    fishShopClient.postOrder(command, state)

    state = OrderState.empty

    SuccessOutCommand()
  }

  private def handleResetOrder(command: InCommand): OutCommand = {
    state = OrderState(state.map filterKeys (_ != command.userName))
    messagePostService.postCurrentState(state, command.responseUrl)
    SuccessOutCommand()
  }

  private def handleOrderMenu(command: InCommand): OutCommand = {
    fishShopClient.fetchMenu(command)

    SuccessOutCommand()
  }

  private def handleOrderCommand(command: InCommand): OutCommand = {
    if (command.text.isEmpty) return ErrorOutCommand("No parameters given")

    try {
      val argsNumbers = command.text.map(l => l.toInt).toSeq
      if (!argsNumbers.forall(l => l <= 5 && l >= 0)) {
        val msg = "Invalid range of arguments [0-5]"
        Logger.error(msg)
        ErrorOutCommand(msg)
      } else {
        val joinedUserState = state.map.getOrElse(command.userName, Seq.empty) ++ argsNumbers
        state = OrderState(state.map + (command.userName -> joinedUserState))

        messagePostService.postCurrentState(state, command.responseUrl)
        SuccessOutCommand()
      }
    } catch {
      case e: NumberFormatException =>
        val msg = "Arguments are not numbers"
        Logger.error(msg, e)
        ErrorOutCommand(msg + s"- ${e.getMessage}")
    }

  }

  private def handleUnknownCommand = ErrorOutCommand("Unknown command")

}
