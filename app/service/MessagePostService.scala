package service

import com.google.inject.Inject
import domain.{OrderState, OutMessage}
import javax.inject.Singleton
import play.Logger
import play.api.libs.json._
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


/**
  *
  * @author Jakub Tucek
  */
@Singleton
class MessagePostService @Inject()(ws: WSClient) {
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val postUrl = "https://hooks.slack.com/services/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"

  def postCurrentState(state: OrderState): Unit = {
    val stateFormatted = state.map map {
      case (key, value) => s"""$key ordered ${value mkString ", "}"""
    } mkString(" • ", "\n", "\n")

    postMessage(OutMessage(
      s"""
         |*Current state of order*:
         |$stateFormatted
      """.stripMargin))
  }

  def postMessage(outMessage: OutMessage): Unit = {
    val request: WSRequest = ws.url(postUrl)
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
