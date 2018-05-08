package domain

/**
  * Represents parsed processing of incoming command
  *
  * @author Jakub Tucek
  */
sealed trait OutCommand

case class SuccessOutCommand() extends OutCommand

case class ErrorOutCommand(message: String) extends OutCommand
