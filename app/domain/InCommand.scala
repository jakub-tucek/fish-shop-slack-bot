package domain

import service.CommandHelper


/**
  * Incoming slack command representation
  *
  * @author Jakub Tucek
  */
case class InCommand(token: String,
                     teamId: String,
                     teamDomain: String,
                     enterpriseId: String,
                     enterpriseName: String,
                     channelId: String,
                     channelName: String,
                     userId: String,
                     userName: String,
                     command: CommandType,
                     text: Seq[String],
                     responseUrl: String,
                     triggerId: String)


object InCommand {

  /**
    * Creates and parses map to InCommand type
    *
    * @param map accepted map from request
    * @return InCommand type
    */
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
      case CommandType.fishOrderCmd => OrderCommand()
      case CommandType.fishMenuCmd => MenuCommand()
      case CommandType.fishResetCmd => ResetOrderCommand()
      case CommandType.fishCompleteCmd => CompleteOrderCommand()
      case CommandType.fishStatusCmd => StatusCommand()
      case _ => UnknownCommand()
    },
    CommandHelper.getHead(map, "text")
      .replace(",", " ")
      .split(" ")
      .filter(x => x.nonEmpty), // split by spaces and filter empty
    CommandHelper getHead(map, "response_url"),
    CommandHelper getHead(map, "trigger_id")
  )

}