package service

import com.google.inject.Inject
import domain.FishShopConfig
import javax.inject.Singleton
import play.api.Configuration

/**
  * Provider of custom config instance.
  *
  * @author Jakub Tucek
  */
@Singleton
class ConfigProvider @Inject()(configuration: Configuration) {


  // Parses configuration properties and returns them as FishShopConfig type
  lazy val config: FishShopConfig = FishShopConfig(
    configuration.get[String]("verificationToken"),
    configuration.get[String]("fishShopMenuUrl"),
    configuration.get[String]("fishShopReservationUrl"),
    configuration.get[String]("fishShopEmail"),
    configuration.get[String]("fishShopName"),
    configuration.get[String]("fishShopPhone")
  )

}

