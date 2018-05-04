package domain

import play.api.libs.json.{JsObject, Json, Writes}


case class OutMessage(text: String)

object OutMessage {
  implicit val outMessageWrites: Writes[OutMessage] = new Writes[OutMessage] {
    def writes(OutMessage: OutMessage): JsObject = Json.obj(
      "text" -> OutMessage.text
    )
  }
}