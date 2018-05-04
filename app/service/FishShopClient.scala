package service

import com.google.inject.Inject
import javax.inject.Singleton
import play.api.Logger
import play.api.libs.ws._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

@Singleton
class FishShopClient @Inject()(ws: WSClient, configProvider: ConfigProvider) {
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  def fetchMenu(): Unit = {
    val request: WSRequest = ws.url(configProvider.config.fishShopMenuUrl)

    request.get.onComplete {
      case Success(response) =>
        handleResponse(response.body)
      case Failure(t) =>
        val msg = "Unable to download current menu"
        Logger.error(msg, t)
    }
  }

  private def handleResponse(response: String): String = {
    println(response)
    ""
  }

}
