package domain

/**
  *
  * @author Jakub Tucek
  */
sealed trait OutCommand

case class SuccessOutCommand() extends OutCommand

case class ErrorOutCommand(message: String) extends OutCommand
