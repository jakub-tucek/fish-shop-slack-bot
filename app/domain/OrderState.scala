package domain


/**
  *
  * @author Jakub Tucek
  */
case class OrderState(map: Map[String, List[Int]])


object OrderState {
  def empty: OrderState = OrderState(Map.empty[String, List[Int]])
}