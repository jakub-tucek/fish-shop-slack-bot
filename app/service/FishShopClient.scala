package service

import com.google.inject.Inject
import domain._
import javax.inject.Singleton
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import play.api.Logger
import play.api.libs.ws._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

@Singleton
class FishShopClient @Inject()(ws: WSClient, messagePostService: MessagePostService, configProvider: ConfigProvider, implicit val ec: ExecutionContext) {

  def postOrder(inCommand: InCommand, state: OrderState): Unit = {
    val conf = configProvider.config
    val form = ReservationForm(conf.fishShopName, conf.fishShopEmail, conf.fishShopPhone, state)

    val request: WSRequest = ws.url(configProvider.config.fishShopReservationUrl)
    val complexRequest: WSRequest = createPostOrderComplexRequest(request)

    Logger.debug(s"Posting form data ${form.getFormData}")

    val body = views.html.reservationBody.render(form.getFormData).body

    complexRequest.post(body).onComplete {
      case Success(response) =>
        Logger.debug(s"Completing order was successful: $response")
        Logger.debug(s"Body: ${response.body}")

        messagePostService.postOrderCreated(inCommand.response_url)
      case Failure(t) => handleError(t, "Completing order failed", inCommand.response_url)
    }
  }

  private def handleError(t: Throwable, msg: String, url: String): Unit = {
    val attachmentMsg = s"$msg - ${t.getMessage}"
    messagePostService.postMessage(OutMessage.create(Attachment("Error!", attachmentMsg, s"Error!: $attachmentMsg", "danger")), url)
    Logger.error(msg, t)
  }

  def fetchMenu(inCommand: InCommand): Unit = {
    val request: WSRequest = ws.url(configProvider.config.fishShopMenuUrl)

    request.get.onComplete {
      case Success(response) => handleResponse(response.body, inCommand.response_url)
      case Failure(t) => handleError(t, "Unable to download current menu", inCommand.response_url)
    }
  }

  /**
    * Fish shop response handler, public due to easier testing
    *
    * @param response html response
    * @return parsed text
    */
  def handleResponse(response: String, responseUrl: String): Unit = {
    val browser = JsoupBrowser()
    val doc = browser.parseString(response)

    val elems = doc >> elementList(".entry-content > p, .entry-content li")

    var dirtyCounter = 0

    val res = elems filter (_.text.nonEmpty) map {
      e => {
        if (e.toString.contains("li")) {
          dirtyCounter += 1
          s" *$dirtyCounter.* ${e.text}"
        } else {
          dirtyCounter = 0
          e.text
        }
      }
    } mkString "\n"


    messagePostService.postMessage(OutMessage.create(
      Attachment("Daily fish shop menu", res, "Daily fish menu cannot be displayed at the moment")
    ), responseUrl)
  }

  private def createPostOrderComplexRequest(request: WSRequest): WSRequest = request.addHttpHeaders(
    "Connection" -> "keep-alive",
    "Accept" -> "application/json, text/javascript, */*; q=0.01",
    "Origin" -> "http://rybarna.net",
    "X-Requested-With" -> "XMLHttpRequest",
    "User-Agent" -> "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36",
    "Content-Type" -> "multipart/form-data; boundary=----WebKitFormBoundarypLGqwPVp389t6dBu",
    "DNT" -> "1",
    "Referer" -> "http://rybarna.net/rezervace/",
    "Accept-Encoding" -> "gzip, deflate",
    "Accept-Language" -> "en-US,en;q=0.9,cs;q=0.8"
  )
}
