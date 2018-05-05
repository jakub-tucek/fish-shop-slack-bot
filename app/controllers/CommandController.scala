package controllers

import domain.{ErrorOutCommand, InCommand, OutMessage, SuccessOutCommand}
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import service.{CommandService, MessagePostService}

@Singleton
class CommandController @Inject()(cc: ControllerComponents, commandService: CommandService, messagePostService: MessagePostService) extends AbstractController(cc) {


  def handleCommand: Action[Map[String, Seq[String]]] = Action(parse.formUrlEncoded) {
    request =>
      val cmd = InCommand.createFromMap(request.body)
      commandService.handleCommand(cmd) match {
        case SuccessOutCommand() => Created
        case ErrorOutCommand(msg) =>
          messagePostService.postMessage(OutMessage(msg))
          NoContent
      }
  }
}
