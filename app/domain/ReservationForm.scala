package domain

/**
  *
  * @author Jakub Tucek
  */
class ReservationForm(val name: String,
                      val email: String,
                      val phone: String,
                      val countSoup: Int = 0,
                      val meal1: Int = 0,
                      val meal2: Int = 0,
                      val meal3: Int = 0,
                      val meal4: Int = 0,
                      val meal5: Int = 0,
                      val note: String = ""
                     ) {


  def getFormData: Map[String, String] = Map(
    "jmeno" -> name,
    "emailadresa" -> email,
    "telefon" -> phone,
    getBoolEntry(countSoup, "polevka[]"),
    getMealCountEntry(countSoup, "KSpolevka"),
    getBoolEntry(meal1, "c1[]"),
    getMealCountEntry(meal1, "c1-ks"),
    getBoolEntry(meal2, "c2[]"),
    getMealCountEntry(meal2, "c2-ks"),
    getBoolEntry(meal3, "c3[]"),
    getMealCountEntry(meal3, "c3-ks"),
    getBoolEntry(meal4, "c4[]"),
    getMealCountEntry(meal4, "c4-ks"),
    getBoolEntry(meal5, "c5[]"),
    getMealCountEntry(meal5, "c5-ks"),
    "poznamka" -> note
  )

  private def getBoolEntry(value: Int, keyName: String) = keyName -> (if (value > 0) "ano" else "")

  private def getMealCountEntry(value: Int, keyName: String) = {
    val valueStr = if (value > 0) value.toString else ""
    keyName -> valueStr
  }
}

object ReservationForm {
  def apply(name: String, phone: String, email: String): ReservationForm = new ReservationForm(name, phone, email)
}
