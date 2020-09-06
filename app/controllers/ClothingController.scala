package controllers

import scala.concurrent.ExecutionContext

import play.api.mvc.ControllerComponents
import play.api.Logging
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent

class ClothingController(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with Logging {

  def search(term: String): Action[AnyContent] = ???
}
