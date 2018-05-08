package service

import com.google.inject.Inject
import domain.{Attachment, OrderState, OutMessage}
import javax.inject.Singleton
import play.Logger
import play.api.libs.json._
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


/**
  * MessagePostService is responsible for posting message to slack channels.
  *
  * @author Jakub Tucek
  */
@Singleton
class MessagePostService @Inject()(ws: WSClient, configProvider: ConfigProvider, implicit val ec: ExecutionContext) {

  /**
    * Posts message about completed order.
    *
    * @param url url where to send message
    */
  def postOrderCreated(url: String): Unit = {
    val conf = configProvider.config
    val status =
      s"""• Name: ${conf.fishShopName}
         | • Phone: ${conf.fishShopPhone}
         | • Email: ${conf.fishShopEmail}
       """.stripMargin

    postMessage(
      OutMessage.create(Attachment("Order was created successfully", status, "Order was created successfully")),
      url
    )
  }

  /**
    * Posts current state to given channel.
    *
    * @param state current state
    * @param url   url where to send message
    */
  def postCurrentState(state: OrderState, url: String): Unit = {
    val attachments = if (state.map.nonEmpty) {
      state.map.map {
        case (key, value) => Attachment(
          key,
          s"_${FishShopUtils.formatOrderItems(value)}_",
          "Orders cannot be displayed at the moment"
        )
      }.toSeq
    } else {
      Seq(Attachment(
        "Fish shop orders",
        "No orders found :confused:",
        "No fish shop orders",
        "warning"
      ))
    }

    postMessage(OutMessage("*Fish shop orders*", attachments), url)
  }

  /**
    * Posts given message to slack channel.
    *
    * @param outMessage out message type
    * @param url        channel url
    */
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
