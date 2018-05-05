package domain

// Trait representing command type
sealed trait CommandType

// New order
case class OrderCommand() extends CommandType

case class MenuCommand() extends CommandType

case class ResetOrderCommand() extends CommandType

case class CompleteOrderCommand() extends CommandType

case class StatusCommand() extends CommandType

case class UnknownCommand() extends CommandType
