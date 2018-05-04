package controllers

import javax.inject._
import play.api.mvc._
import service.MessagePostService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, messagePostService: MessagePostService) extends AbstractController(cc) {


  def index() = Action { implicit request: Request[AnyContent] =>
    //    messagePostService.postMessage(OutMessage("Hey"))

    Ok(views.html.index())

  }
}
