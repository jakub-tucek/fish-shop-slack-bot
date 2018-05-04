package domain


/**
  *
  * @author Jakub Tucek
  */
case class OrderState(map: Map[String, Set[Int]])


object OrderState {
  def empty: OrderState = OrderState(Map.empty[String, Set[Int]])
}