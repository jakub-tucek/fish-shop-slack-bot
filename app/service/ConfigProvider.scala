package service

import com.google.inject.Inject
import domain.FishShopConfig
import javax.inject.Singleton
import play.api.Configuration

/**
  *
  * @author Jakub Tucek
  */
@Singleton
class ConfigProvider @Inject()(configuration: Configuration) {


  lazy val config: FishShopConfig = FishShopConfig(
    configuration.get[String]("messageUrl"),
    configuration.get[String]("verificationToken"),
    configuration.get[String]("fishShopMenuUrl"),
    configuration.get[String]("fishShopReservationUrl"),
    configuration.get[String]("fishShopEmail"),
    configuration.get[String]("fishShopName"),
    configuration.get[String]("fishShopPhone")
  )

}

