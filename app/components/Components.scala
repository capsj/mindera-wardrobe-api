package components

import com.softwaremill.macwire.wire
import com.typesafe.config.Config
import controllers.AssetsComponents
import controllers.ClothingController
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router
import router.Routes

class Components(context: Context)
  extends BuiltInComponentsFromContext(context)
    with AhcWSComponents
    with SlickDatabaseComponents
    with AssetsComponents
    with HttpComponents {

//  implicit lazy val clock: Clock                      = Clock.defaultClock
  lazy val config: Config                             = configuration.underlying

  lazy val clothingController = wire[ClothingController]
  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }

  migrateDatabase()
}

