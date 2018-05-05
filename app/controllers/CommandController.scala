package controllers

import domain.{ErrorOutCommand, InCommand, SuccessOutCommand}
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import service.CommandService

@Singleton
class CommandController @Inject()(cc: ControllerComponents, commandService: CommandService) extends AbstractController(cc) {


  def handleCommand: Action[Map[String, Seq[String]]] = Action(parse.formUrlEncoded) {
    request =>
      val cmd = InCommand.createFromMap(request.body)
      commandService.handleCommand(cmd) match {
        case SuccessOutCommand() => Created
        case ErrorOutCommand(msg) => NoContent
      }
      Created
  }
}
