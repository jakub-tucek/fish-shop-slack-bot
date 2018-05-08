package domain

import play.api.libs.json.{JsObject, JsValue, Json, Writes}


case class OutMessage(text: String, attachments: Seq[Attachment])

case class Attachment(title: String, text: String, fallback: String, color: String = "#7CD197")

object OutMessage {
  implicit val outMessageWrites: Writes[OutMessage] = new Writes[OutMessage] {
    def writes(outMessage: OutMessage): JsObject = Json.obj(
      "text" -> outMessage.text,
      "attachments" -> outMessage.attachments.map(a => Attachment.attachmentWrites.writes(a))
    )
  }

  def create(attachment: Attachment) = OutMessage("", Seq(attachment))
}

object Attachment {
  implicit val attachmentWrites: Writes[Attachment] = new Writes[Attachment] {
    override def writes(o: Attachment): JsValue = Json.obj(
      "title" -> o.title,
      "text" -> o.text,
      "fallback" -> o.fallback,
      "color" -> o.color
    )
  }
}