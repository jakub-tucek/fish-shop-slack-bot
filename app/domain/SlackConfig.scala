package domain

/**
  *
  * @author Jakub Tucek
  */
case class SlackConfig(messageUrl: String,
                       oauthToken: String
                      )


object SlackConfig {

  def getConfig: SlackConfig = SlackConfig(
      "https://hooks.slack.com/services/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",
    "xoxp-qwqewewqeqwqeweqw"
  )
}