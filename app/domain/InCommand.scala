package domain

import service.CommandHelper

/**
  *
  * @author Jakub Tucek
  */
case class InCommand(token: String,
                     team_id: String,
                     team_domain: String,
                     enterprise_id: String,
                     enterprise_name: String,
                     channel_id: String,
                     channel_name: String,
                     user_id: String,
                     user_name: String,
                     command: CommandType,
                     text: Seq[String],
                     response_url: String,
                     trigger_id: String)


object InCommand {

  def createFromMap(map: Map[String, Seq[String]]): InCommand = InCommand(
    CommandHelper getHead(map, "token"),
    CommandHelper getHead(map, "team_id"),
    CommandHelper getHead(map, "team_domain"),
    CommandHelper getHead(map, "enterprise_id"),
    CommandHelper getHead(map, "enterprise_name"),
    CommandHelper getHead(map, "channel_id"),
    CommandHelper getHead(map, "channel_name"),
    CommandHelper getHead(map, "user_id"),
    CommandHelper getHead(map, "user_name"),
    CommandHelper getHead(map, "command") match {
      case "/order" => OrderCommand()
      case _ => UnknownCommand()
    },
    CommandHelper getList(map, "text"),
    CommandHelper getHead(map, "response_url"),
    CommandHelper getHead(map, "trigger_id")
  )

}