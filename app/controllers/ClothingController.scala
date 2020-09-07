package controllers

import scala.concurrent.ExecutionContext

import components.WritableInstances
import io.scalaland.chimney.dsl._
import models.data.DataService
import models.api.ClothingItem
import models.api.SearchClothingItemResponse
import play.api.mvc.ControllerComponents
import play.api.Logging
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent

class ClothingController(cc: ControllerComponents, dataService: DataService)(implicit ec: ExecutionContext)
  extends AbstractController(cc)
  with WritableInstances
  with Logging {

  def searchByName(term: String): Action[AnyContent] =
    Action.async { _ =>
      dataService
        .searchByName(term)
        .map(_.map(_.transformInto[ClothingItem]))
        .map(Ok(_))
    }


}
