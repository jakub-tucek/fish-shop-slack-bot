package domain


/**
  *
  * @author Jakub Tucek
  */
case class OrderState(map: Map[String, Seq[Int]])


object OrderState {
  def empty: OrderState = OrderState(Map.empty[String, Seq[Int]])
}