package service

import com.google.inject.Inject
import domain._
import javax.inject.Singleton
import play.api.Logger

/**
  *
  * @author Jakub Tucek
  */
@Singleton
class CommandService @Inject()(messagePostService: MessagePostService) {

  private var state: OrderState = OrderState.empty

  def handleCommand(command: InCommand): OutCommand = {
    command.command match {
      case OrderCommand() => handleOrderCommand(command)
      case MenuCommand() => handleOrderMenu(command)
      case UnknownCommand() => handleUnknownCommand
    }
  }


  private def handleOrderMenu(command: InCommand): OutCommand = {


    SuccessOutCommand()
  }

  private def handleOrderCommand(command: InCommand): OutCommand = {
    if (command.text.isEmpty) ErrorOutCommand("No parameters given")

    try {
      val argsNumbers = command.text.map(l => l.toInt).toSet
      if (!argsNumbers.forall(l => l < 5 && l >= 0)) {
        val msg = "Invalid range of arguments [0-5]"
        Logger.error(msg)
        ErrorOutCommand(msg)
      } else {
        state = OrderState(state.map + (command.user_name -> argsNumbers))

        messagePostService.postCurrentState(state)
        SuccessOutCommand()
      }
    } catch {
      case e: NumberFormatException =>
        val msg = "Arguments are not numbers"
        Logger.error(msg, e)
        ErrorOutCommand(msg + "- ${e.getMessage}")
    }

  }

  private def handleUnknownCommand = {
    ErrorOutCommand("Unknown out command")
  }
}
