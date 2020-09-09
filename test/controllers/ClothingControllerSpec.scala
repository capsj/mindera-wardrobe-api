package controllers

import java.nio.file.{Files => JFiles}
import java.time.Instant

import scala.concurrent.Future
import scala.util.Random

import akka.http.scaladsl.model.MediaTypes
import akka.stream.scaladsl.Source
import akka.Done
import io.scalaland.chimney.dsl._
import data.CategoryRow
import data.ClothingItemRow
import data.ClothingItemViewRow
import data.OutfitRow
import models.ClothingItem
import models.ClothingItemView
import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status.OK
import play.api.libs.json.JsSuccess
import play.api.libs.Files.SingletonTemporaryFileCreator
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData
import play.api.mvc.MultipartFormData.FilePart
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ClothingControllerSpec extends ControllerSpec with ScalaFutures {

  behavior of "ClothingController"

  it should "allow searching clothing items by name" in {
      val term: String = Random.nextString(10)
      val clothingItems = Seq.range(1, 5).map({ i => ClothingItemRow(i, term, Instant.EPOCH, Instant.EPOCH) })

      (dataService.searchByName _)
        .expects(term)
        .returns(Future.successful(clothingItems))

      val fakeRequest = FakeRequest(routes.ClothingController.searchByName(term))
      val expectedResponse = clothingItems.map(_.transformInto[ClothingItem])

      inside(route(app, fakeRequest)) {
        case Some(result) =>
          status(result) shouldEqual OK
          inside(contentAsJson(result).validate[Seq[ClothingItem]]) {
            case JsSuccess(response, _) =>
              response shouldBe expectedResponse
          }
      }
  }

  it should "allow listing full views of all clothing items" in {
    val categories = Seq.range(1, 5).map({ i => CategoryRow(i, Random.nextString(10)) })
    val outfits = Seq.range(1, 5).map({ i => OutfitRow(i, Random.nextString(10)) })
    val clothingItems = Seq.range(1, 5).map({ i => ClothingItemRow(i, Random.nextString(10), Instant.EPOCH, Instant.EPOCH) })
    val clothingItemViewRows = clothingItems.map { item =>
      ClothingItemViewRow(item, categories, outfits)
    }

    (dataService.getClothingListView _)
      .expects()
      .returns(Future.successful(clothingItemViewRows))

    val fakeRequest = FakeRequest(routes.ClothingController.listClothingItems())
    val expectedResponse = clothingItemViewRows.map(_.transformInto[ClothingItemView])

    inside(route(app, fakeRequest)) {
      case Some(result) =>
        status(result) shouldEqual OK
        inside(contentAsJson(result).validate[Seq[ClothingItemView]]) {
          case JsSuccess(response, _) =>
            response shouldBe expectedResponse
        }
    }
  }

  it should "allow clothing items and categories to be uploaded via a csv file" in {
    /* Set up temporary file to be uploaded */
    val tempFile = SingletonTemporaryFileCreator.create("clothing", "csv")
    /* Write something in it, otherwise it will be ignored */
    val fileSize = 1024
    val buffer = Array.ofDim[Byte](fileSize)
    new Random(0).nextBytes(buffer)
    JFiles.write(tempFile.path, buffer)

    val uploadedFile: FilePart[TemporaryFile] = FilePart("key", "name", Some(MediaTypes.`text/csv`.value), tempFile, fileSize)
    val csvRow = CsvRow("clothingItemName", "categoryName")

    (csvReader.parseCsv _)
      .expects(* /* uploadedFile */)
      .returning(Source.fromIterator(() => Seq(Some(csvRow)).iterator))

    (dataService.insertClothingItem _)
      .expects(csvRow.clothingItemName, csvRow.categoryName)
      .returns(Future.successful(Done))

    val fakeRequest = FakeRequest(routes.ClothingController.uploadCsv())
      .withMultipartFormDataBody(MultipartFormData(
        dataParts = Map.empty,
        files = Seq(uploadedFile),
        Seq.empty
      ))

    inside(route(app, fakeRequest)) {
      case Some(result) =>
        status(result) shouldEqual OK
    }
  }

  it should "allow tagging clothing items as part of an outfit" in {
    val clothingItemId = 1
    val outfitName = "outfit"

    (dataService.tagClothingItem _)
      .expects(clothingItemId, outfitName)
      .returns(Future.successful(Done))

    val fakeRequest = FakeRequest(routes.ClothingController.tagClothingItem(clothingItemId, outfitName))
    inside(route(app, fakeRequest)) {
      case Some(result) =>
        status(result) shouldEqual OK
    }
  }
}
