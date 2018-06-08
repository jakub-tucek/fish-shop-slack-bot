package domain

import service.FishShopUtils

import scala.collection.mutable

/**
  * Class representing fish shop reservation form.
  *
  * @author Jakub Tucek
  */
class ReservationForm(val name: String,
                      val email: String,
                      val phone: String,
                      mealCounts: Map[MealType, Int], // maps meal id with required quantity
                      val note: String = ""
                     ) {


  /**
    * Converts internal object properties to linked hash map. Returned as map
    * which keeps given key order.
    *
    * @return linked hash map where key corresponds with it's input name + value
    */
  def getFormData: mutable.LinkedHashMap[String, String] = mutable.LinkedHashMap(
    "jmeno" -> name,
    "emailadresa" -> email,
    "telefon" -> phone,
    getBoolEntry(mealCounts.getOrElse(SoupMealType, 0), "polevka[]"),
    getMealCountEntry(mealCounts.getOrElse(SoupMealType, 0), "KSpolevka"),
    getBoolEntry(mealCounts.getOrElse(FirstMealType, 0), "c1[]"),
    getMealCountEntry(mealCounts.getOrElse(FirstMealType, 0), "c1-ks"),
    getBoolEntry(mealCounts.getOrElse(SecondMealType, 0), "c2[]"),
    getMealCountEntry(mealCounts.getOrElse(SecondMealType, 0), "c2-ks"),
    getBoolEntry(mealCounts.getOrElse(ThirdMealType, 0), "c3[]"),
    getMealCountEntry(mealCounts.getOrElse(ThirdMealType, 0), "c3-ks"),
    getBoolEntry(mealCounts.getOrElse(FourthMealType, 0), "c4[]"),
    getMealCountEntry(mealCounts.getOrElse(FourthMealType, 0), "c4-ks"),
    getBoolEntry(mealCounts.getOrElse(FifthMealType, 0), "c5[]"),
    getMealCountEntry(mealCounts.getOrElse(FifthMealType, 0), "c5-ks"),
    "poznamka" -> note
  ).filter { case (key, value) => value != "" }

  // maps meal count to required string representation
  private def getBoolEntry(value: Int, keyName: String) = keyName -> (if (value > 0) "ano" else "")

  private def getMealCountEntry(value: Int, keyName: String) = {
    val valueStr = if (value > 0) value.toString else ""
    keyName -> valueStr
  }
}

object ReservationForm {
  def apply(name: String, email: String, phone: String, state: OrderState): ReservationForm = {
    val mealCounts: Map[MealType, Int] = FishShopUtils.countOccurrence(state.map.values.toSeq.flatten)

    new ReservationForm(
      name,
      email,
      phone,
      mealCounts
    )
  }
}
