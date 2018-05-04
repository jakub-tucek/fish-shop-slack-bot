package controllers

import domain.InCommand
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.json.{JsValue, Reads}
import play.api.mvc.{AbstractController, ControllerComponents, Request, Result}

@Singleton
class CommandController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  def createOrder = Action(parse.formUrlEncoded) {
    request =>
      println(request.body)
      println(InCommand.createFromMap(request.body))
      Created
  }
}
