package domain


// meal type abstract representation
sealed trait MealType {
  val mealId: Int
  val emoji: String
}

case object SoupMealType extends MealType {
  override val mealId: Int = 0
  override val emoji: String = ":ramen:"
}

case object FirstMealType extends MealType {
  override val mealId: Int = 1
  override val emoji: String = ":fish:"
}

case object SecondMealType extends MealType {
  override val mealId: Int = 2
  override val emoji: String = ":fish:"
}

case object ThirdMealType extends MealType {
  override val mealId: Int = 3
  override val emoji: String = ":curry:"
}


case object FourthMealType extends MealType {
  override val mealId: Int = 4
  override val emoji: String = ":curry:"
}

case object FifthMealType extends MealType {
  override val mealId: Int = 5
  override val emoji: String = "green_salad"
}

case object UnknownMealType extends MealType {
  override val mealId: Int = 666
  override val emoji: String = ":grey_question:"
}

object MealType {
  def getTypeById(mealId: Int): MealType = {
    mealId match {
      case 0 => SoupMealType
      case 1 => FirstMealType
      case 2 => SecondMealType
      case 3 => ThirdMealType
      case 4 => FourthMealType
      case 5 => FifthMealType
      case _ => UnknownMealType
    }
  }
}