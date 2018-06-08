package domain


/**
  * Current order state. Keeps map of [Username, list of ordered items identification]
  *
  * @author Jakub Tucek
  */
case class OrderState(map: Map[String, Seq[MealType]])


object OrderState {

  // empty state
  def empty: OrderState = OrderState(Map.empty[String, Seq[MealType]])
}