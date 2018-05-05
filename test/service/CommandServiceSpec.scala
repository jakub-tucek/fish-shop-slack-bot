package service

import domain.{ErrorOutCommand, InCommand, OrderState, SuccessOutCommand}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

/**
  *
  * @author Jakub Tucek
  */
class CommandServiceSpec extends FlatSpec
  with Matchers
  with MockFactory
  with BeforeAndAfterEach {

  private var fishShopClient = stub[FishShopClient]
  private var messagePostService = stub[MessagePostService]
  private var commandService: CommandService = new CommandService(messagePostService, fishShopClient)

  override def beforeEach(): Unit = {
    var fishShopClient = stub[FishShopClient]
    var messagePostService = stub[MessagePostService]
    var commandService: CommandService = new CommandService(messagePostService, fishShopClient)
  }

  "Reset command" should "Reset empty state" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishResetCmd))
    )

    val res = commandService.handleCommand(cmd)

    res shouldBe SuccessOutCommand()

    commandService.state shouldBe OrderState.empty
  }

  "Reset command" should "Reset user's order" in {
    commandService.state = OrderState(Map("usr" -> Set(1, 2)))

    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishResetCmd),
      "user_name" -> Seq("usr"))
    )

    val res = commandService.handleCommand(cmd)
    commandService.state shouldBe OrderState.empty
  }

  "Unknown command" should "throw error" in {
    val cmd = InCommand.createFromMap(Map())
    val res = commandService.handleCommand(cmd)
    res shouldBe ErrorOutCommand("Unknown command")
  }

  "Menu command" should "call fish client to fetch menu" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishMenuCmd))
    )
    val res = commandService.handleCommand(cmd)

    res shouldBe SuccessOutCommand()

    (fishShopClient.fetchMenu _).verify()
  }

  "Order command" should "fail if no params are given" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishOrderCmd),
      "text" -> Seq())
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe ErrorOutCommand("No parameters given")
  }

  "Order command" should "fail if params are let then 0" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishOrderCmd),
      "text" -> Seq("-1"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe ErrorOutCommand("Invalid range of arguments [0-5]")
  }

  "Order command" should "fail if params are not numbers" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishOrderCmd),
      "text" -> Seq("ahoj"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe ErrorOutCommand("Arguments are not numbers- For input string: \"ahoj\"")
  }

  "Order command" should "modify state correctly" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishOrderCmd),
      "user_name" -> Seq("jt"),
      "text" -> Seq("1", "2"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe SuccessOutCommand()

    commandService.state shouldBe OrderState(Map("jt" -> Set(1, 2)))
  }

  "Order command" should "modify state without duplicated values" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishOrderCmd),
      "user_name" -> Seq("jt"),
      "text" -> Seq("1", "2", "1", "2"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe SuccessOutCommand()

    commandService.state shouldBe OrderState(Map("jt" -> Set(1, 2)))
  }

  "Order command" should "call post service with new state" in {
    val cmd = InCommand.createFromMap(Map(
      "command" -> Seq(InCommand.fishOrderCmd),
      "user_name" -> Seq("jt"),
      "text" -> Seq("1", "2"))
    )

    val res = commandService.handleCommand(cmd)
    res shouldBe SuccessOutCommand()

    (messagePostService.postCurrentState _).verify(commandService.state)
  }

  override def afterEach(): Unit = {}
}