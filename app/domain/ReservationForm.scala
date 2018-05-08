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
                      mealCounts: Map[Int, Int], // maps meal id with required quantity
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
    getBoolEntry(mealCounts.getOrElse(0, 0), "polevka[]"),
    getMealCountEntry(mealCounts.getOrElse(0, 0), "KSpolevka"),
    getBoolEntry(mealCounts.getOrElse(1, 0), "c1[]"),
    getMealCountEntry(mealCounts.getOrElse(1, 0), "c1-ks"),
    getBoolEntry(mealCounts.getOrElse(2, 0), "c2[]"),
    getMealCountEntry(mealCounts.getOrElse(2, 0), "c2-ks"),
    getBoolEntry(mealCounts.getOrElse(3, 0), "c3[]"),
    getMealCountEntry(mealCounts.getOrElse(3, 0), "c3-ks"),
    getBoolEntry(mealCounts.getOrElse(4, 0), "c4[]"),
    getMealCountEntry(mealCounts.getOrElse(4, 0), "c4-ks"),
    getBoolEntry(mealCounts.getOrElse(5, 0), "c5[]"),
    getMealCountEntry(mealCounts.getOrElse(5, 0), "c5-ks"),
    "poznamka" -> note
  )

  private def getBoolEntry(value: Int, keyName: String) = keyName -> (if (value > 0) "ano" else "")

  private def getMealCountEntry(value: Int, keyName: String) = {
    val valueStr = if (value > 0) value.toString else ""
    keyName -> valueStr
  }
}

object ReservationForm {
  def apply(name: String, email: String, phone: String, state: OrderState): ReservationForm = {
    val mealCounts: Map[Int, Int] = FishShopUtils.countOccurrence(state.map.values.toArray.flatten)

    new ReservationForm(
      name,
      email,
      phone,
      mealCounts
    )
  }
}
