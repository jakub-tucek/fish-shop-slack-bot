package domain

sealed trait CommandType

case class OrderCommand() extends CommandType

case class UnknownCommand() extends CommandType

case class MenuCommand() extends CommandType
