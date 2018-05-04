package controllers

import domain.OutMessage
import javax.inject._
import play.api.mvc._
import service.MessagePostService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, messagePostService: MessagePostService) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    messagePostService.postMessage(OutMessage("Hey"))

    Ok(views.html.index())

  }
}
