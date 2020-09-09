package controllers

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.stream.scaladsl.Sink
import akka.stream.Materializer
import akka.Done
import cats.data.EitherT
import cats.instances.future._
import components.WritableInstances
import io.scalaland.chimney.dsl._
import data.DataService
import models.ClothingItem
import models.ClothingItemView
import play.api.mvc.ControllerComponents
import play.api.libs.Files.TemporaryFile
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.MultipartFormData

class ClothingController(cc: ControllerComponents, dataService: DataService, csvReader: CsvReader)(implicit
  ec: ExecutionContext,
  materializer: Materializer
) extends AbstractController(cc)
  with WritableInstances {

  def searchByName(term: String): Action[AnyContent] =
    Action.async { _ =>
      dataService
        .searchByName(term)
        .map(_.map(_.transformInto[ClothingItem]))
        .map(Ok(_))
    }

  def listClothingItems: Action[AnyContent] =
    Action.async { _ =>
      dataService.getClothingListView
        .map(_.map(_.transformInto[ClothingItemView]))
        .map(Ok(_))
    }

  /** Upload a CSV file containing clothing items and their categories */
  def uploadCsv: Action[MultipartFormData[TemporaryFile]] =
    Action.async(parse.multipartFormData) { request =>
      (for {
        file <- EitherT.fromOption[Future](request.body.files.headOption, BadRequest(""))
        source =
          csvReader
            .parseCsv(file)
            .map({
              case Some(CsvRow(clothingItemName, categoryName)) =>
                dataService.insertClothingItem(clothingItemName, categoryName)
              case None =>
                Future.successful(Done)
            })
        _ <- EitherT(
          source
            .runWith(Sink.ignore)
            .map(Right(_))
            .recover(_ => Left(InternalServerError(""))))
      } yield Ok).merge
    }

  def tagClothingItem(itemId: Int, outfitName: String) =
    Action.async { _ =>
      dataService
        .tagClothingItem(itemId, outfitName)
        .map(_ => Ok)
    }

}
