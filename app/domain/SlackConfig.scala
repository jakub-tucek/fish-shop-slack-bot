package domain

/**
  *
  * @author Jakub Tucek
  */
case class SlackConfig(messageUrl: String,
                       oauthToken: String
                      )


object SlackConfig {

  def getConfig() = SlackConfig(
      "https://hooks.slack.com/services/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",
      "xoxp-136653274882-264025549393-358698993989-11f02dcbc966dd0d42b31604825d2c23"
  )
}