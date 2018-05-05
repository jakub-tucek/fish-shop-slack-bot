package controllers

import domain.ReservationForm
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
      1,
      1,
      0,
      0,
      0,
      2
    )

    val mappedForm = form.getFormData

    mappedForm should contain("jmeno" -> "name")
    mappedForm should contain("emailadresa" -> "mail")
    mappedForm should contain("telefon" -> "phone")

    mappedForm should contain("polevka[]" -> "ano")
    mappedForm should contain("KSpolevka" -> "1")

    mappedForm should contain("c1[]" -> "ano")
    mappedForm should contain("c1-ks" -> "1")

    mappedForm should contain("c2[]" -> "")
    mappedForm should contain("c2-ks" -> "")

    mappedForm should contain("c3[]" -> "")
    mappedForm should contain("c3-ks" -> "")

    mappedForm should contain("c4[]" -> "")
    mappedForm should contain("c4-ks" -> "")

    mappedForm should contain("c5[]" -> "ano")
    mappedForm should contain("c5-ks" -> "2")

    mappedForm should contain("poznamka" -> "")
  }
}
