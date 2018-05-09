package controllers

import domain._
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsObject, JsString}
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import service.{CommandService, MessagePostService}

@Singleton
class CommandController @Inject()(cc: ControllerComponents, commandService: CommandService, messagePostService: MessagePostService) extends AbstractController(cc) {

  private lazy val defaultResponse = JsObject(Map(
    "response_type" -> JsString("in_channel")
  )
  )

  def handleCommand: Action[Map[String, Seq[String]]] = Action(parse.formUrlEncoded) {
    request =>
      val cmd = InCommand.createFromMap(request.body)
      commandService.handleCommand(cmd) match {
        case SuccessOutCommand() => Created.apply(defaultResponse)
        case ErrorOutCommand(msg) =>
          messagePostService.postMessage(OutMessage.create(Attachment("Error!", msg, s"Error!: $msg :sweat:", "danger")), cmd.responseUrl)
          NoContent
      }
  }
}
