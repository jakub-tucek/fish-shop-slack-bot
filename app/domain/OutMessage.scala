package domain

import play.api.libs.json.{JsObject, JsValue, Json, Writes}

// outgoing slack message type
case class OutMessage(text: String, attachments: Seq[Attachment])

// attachment type, part of OutMessage
case class Attachment(title: String, text: String, fallback: String, color: String = "#7CD197")


object OutMessage {
  // writer definition for json serialization
  implicit val outMessageWrites: Writes[OutMessage] = new Writes[OutMessage] {
    def writes(outMessage: OutMessage): JsObject = Json.obj(
      "text" -> outMessage.text,
      "attachments" -> outMessage.attachments.map(a => Attachment.attachmentWrites.writes(a))
    )
  }

  // helper for creation of out message
  def create(attachment: Attachment) = OutMessage("", Seq(attachment))
}

object Attachment {

  // writer definition for json serialization
  implicit val attachmentWrites: Writes[Attachment] = new Writes[Attachment] {
    override def writes(o: Attachment): JsValue = Json.obj(
      "title" -> o.title,
      "text" -> o.text,
      "fallback" -> o.fallback,
      "color" -> o.color
    )
  }
}