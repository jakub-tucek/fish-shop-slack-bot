package controllers

import domain.{FifthMealType, FirstMealType, ReservationForm, SoupMealType}
import org.scalatest.{FlatSpec, Matchers}

/**
  *
  * @author Jakub Tucek
  */
class ReservationFormSpec extends FlatSpec with Matchers {

  "Reservation Form" should "encode values to form data" in {
    val form = new ReservationForm(
      "name",
      "mail",
      "phone",
      Map(SoupMealType -> 1, FifthMealType -> 2, FirstMealType -> 1)
    )

    val mappedForm = form.getFormData

    mappedForm should contain("jmeno" -> "name")
    mappedForm should contain("emailadresa" -> "mail")
    mappedForm should contain("telefon" -> "phone")

    mappedForm should contain("polevka[]" -> "ano")
    mappedForm should contain("KSpolevka" -> "1")

    mappedForm should contain("c1[]" -> "ano")
    mappedForm should contain("c1-ks" -> "1")

    mappedForm should contain("c5[]" -> "ano")
    mappedForm should contain("c5-ks" -> "2")
  }
}
