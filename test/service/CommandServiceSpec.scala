package service

import com.typesafe.config.ConfigFactory
import domain._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}
import play.api.Configuration

/**
  *
  * @author Jakub Tucek
  */
class CommandServiceSpec extends FlatSpec
  with Matchers
  with MockFactory
  with BeforeAndAfterEach {

  private val tokenEntry = ("token", Seq("verfication token url"))
  private val usernameEntry = ("user_name", Seq("jt"))
  private val messageUrl = ("response_url", Seq("response url"))

  private var fishShopClient = stub[FishShopClient]
  private var messagePostService = stub[MessagePostService]
  private var configProvider = new ConfigProvider(Configuration(ConfigFactory.load("test.conf")))
  private var commandService = new CommandService(messagePostService, fishShopClient, configProvider)

  override def beforeEach(): Unit = {
    fishShopClient = stub[FishShopClient]
    messagePostService = stub[MessagePostService]
    configProvider = new ConfigProvider(Configuration(ConfigFactory.load("test.conf")))
    commandService = new CommandService(messagePostService, fishShopClient, configProvider)
  }

  "Reset command" should "Reset empty state" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishResetCmd),
      tokenEntry
    ))

    val res = commandService.handleCommand(cmd)

    res shouldBe SuccessOutCommand()

    commandService.state shouldBe OrderState.empty
  }

  "Reset command" should "Reset user's order" in {
    commandService.state = OrderState(Map(
      "jt" -> Seq(1, 2)
    ))

    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishResetCmd),
      tokenEntry, messageUrl,
      usernameEntry
    ))

    val res = commandService.handleCommand(cmd)
    commandService.state shouldBe OrderState.empty
  }

  "Unknown command" should "throw error" in {
    val cmd = InCommand.createFromMap(Map(tokenEntry))
    val res = commandService.handleCommand(cmd)
    res shouldBe ErrorOutCommand("Unknown command")
  }

  "Menu command" should "call fish client to fetch menu" in {
    val cmd = InCommand.createFromMap(Map(
      tokenEntry, messageUrl,
      "command" -> Seq(InCommand.fishMenuCmd))
    )
    val res = commandService.handleCommand(cmd)

    res shouldBe SuccessOutCommand()

    (fishShopClient.fetchMenu _).verify(cmd)
  }

  "Order command" should "fail if no params are given" in {
    val cmd = InCommand.createFromMap(Map(
      tokenEntry, messageUrl,
      "command" -> Seq(InCommand.fishOrderCmd),
      "text" -> Seq())
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe ErrorOutCommand("No parameters given")
  }

  "Order command" should "fail if params are let then 0" in {
    val cmd = InCommand.createFromMap(Map(
      tokenEntry, messageUrl,
      "command" -> Seq(InCommand.fishOrderCmd),
      "text" -> Seq("-1"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe ErrorOutCommand("Invalid range of arguments [0-5]")
  }

  "Order command" should "fail if params are not numbers" in {
    val cmd = InCommand.createFromMap(Map(
      tokenEntry, messageUrl,
      "command" -> Seq(InCommand.fishOrderCmd),
      "text" -> Seq("ahoj"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe ErrorOutCommand("Arguments are not numbers- For input string: \"ahoj\"")
  }

  "Order command" should "modify state correctly" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishOrderCmd),
      tokenEntry, messageUrl,
      usernameEntry,
      "text" -> Seq("1 2"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe SuccessOutCommand()

    commandService.state shouldBe OrderState(Map("jt" -> Seq(1, 2)))
  }

  "Order command" should "modify state without duplicated values" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishOrderCmd),
      tokenEntry, messageUrl,
      usernameEntry,
      "text" -> Seq("1 2"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe SuccessOutCommand()

    commandService.state shouldBe OrderState(Map("jt" -> Seq(1, 2)))
  }

  "Order command" should "call post service with new state" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishOrderCmd),
      tokenEntry, messageUrl,
      usernameEntry,
      "text" -> Seq("1 2"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe SuccessOutCommand()

    (messagePostService.postCurrentState _).verify(commandService.state, cmd.response_url)
  }

  "Any command" should "be denied if it has invalid token" in {
    val cmd = InCommand.createFromMap(Map())

    val res = commandService.handleCommand(cmd)
    res shouldBe ErrorOutCommand("Unauthorized token in received command")
  }

  "Complete order command" should "fail if map is empty" in {
    val cmd = InCommand.createFromMap(Map(tokenEntry, messageUrl, "command" -> Seq(InCommand.fishCompleteCmd)))

    val res = commandService.handleCommand(cmd)

    res shouldBe ErrorOutCommand("No orders are saved")
  }

  "Complete order command" should "state entries has empty values is empty" in {
    val cmd = InCommand.createFromMap(Map(tokenEntry, messageUrl, "command" -> Seq(InCommand.fishCompleteCmd)))

    commandService.state = OrderState(Map("jt" -> Seq()))
    val res = commandService.handleCommand(cmd)

    res shouldBe ErrorOutCommand("No orders are saved")
  }

  "Complete order command" should "call client and reset state" in {
    val cmd = InCommand.createFromMap(Map(tokenEntry, messageUrl, "command" -> Seq(InCommand.fishCompleteCmd)))

    val currState = OrderState(Map("jt" -> Seq(1, 2)))
    commandService.state = currState
    val res = commandService.handleCommand(cmd)

    (fishShopClient.postOrder _).verify(cmd, currState)
    res shouldBe SuccessOutCommand()
    commandService.state shouldBe OrderState.empty
  }

  override def afterEach(): Unit = {}
}
