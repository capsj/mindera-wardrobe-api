package controllers

import com.softwaremill.macwire.wire
import com.typesafe.config.Config
import components.HttpComponents
import models.data.DataService
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.routing.Router
import router.Routes

abstract class TestComponents(context: Context) extends BuiltInComponentsFromContext(context) with AssetsComponents with HttpComponents {

  def dataService: DataService

  lazy val config: Config     = configuration.underlying
  lazy val clothingController = wire[ClothingController]
  lazy val router: Router = {
    val prefix: String = "/"
    wire[Routes]
  }
}
