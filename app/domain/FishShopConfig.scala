package domain

/**
  * Case class storing read configuration properties.
  *
  * @author Jakub Tucek
  */
case class FishShopConfig(verificationToken: String,
                          fishShopMenuUrl: String,
                          fishShopReservationUrl: String,
                          fishShopEmail: String,
                          fishShopName: String,
                          fishShopPhone: String
                         )
