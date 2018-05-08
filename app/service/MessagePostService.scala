package service

import com.google.inject.Inject
import domain.{OrderState, OutMessage}
import javax.inject.Singleton
import play.Logger
import play.api.libs.json._
import play.api.libs.ws._
import views.html.currentStateMessage

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


/**
  *
  * @author Jakub Tucek
  */
@Singleton
class MessagePostService @Inject()(ws: WSClient, configProvider: ConfigProvider, implicit val ec: ExecutionContext) {

  def postCurrentState(state: OrderState, url: String): Unit = postMessage(
    OutMessage(views.html.currentStateMessage.render(state).body), url
  )


  def postMessage(outMessage: OutMessage, url: String): Unit = {
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest =
      request.addHttpHeaders("Accept" -> "application/json")

    val data = Json.toJson(outMessage)

    Logger.debug("Sending new message")
    val future: Future[WSResponse] = request.post(data)

    future onComplete {
      case Success(posts) => Logger.debug("Sending message was successful")
      case Failure(t) => Logger.error(s"Sending message failed: ${t.getMessage}")
    }
  }


}
