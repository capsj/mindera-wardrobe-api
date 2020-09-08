package controllers

import java.time.Instant

import scala.concurrent.Future
import scala.util.Random

import io.scalaland.chimney.dsl._
import models.api.Category
import models.api.ClothingItem
import models.api.ClothingItemView
import models.api.Outfit
import models.data.CategoryRow
import models.data.ClothingItemRow
import models.data.ClothingItemViewRow
import models.data.OutfitRow
import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status.OK
import play.api.libs.json.JsSuccess
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ClothingControllerSpec extends ControllerSpec with ScalaFutures {

  "ClothingController" - {
    "searchByName" - {
      "should respond with a list of clothing items" in {
        val term: String = Random.nextString(10)
        val clothingItems = Seq.fill(4)(ClothingItemRow(Some(1), term, Instant.EPOCH, Instant.EPOCH))

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
    }

    "listClothingItems" - {
      "should respond with a full view of the clothing items" in {
        val categories = Seq.range(1, 5).map({ i => CategoryRow(Some(i), Random.nextString(10)) })
        val outfits = Seq.range(1, 5).map({ i => OutfitRow(Some(i), Random.nextString(10)) })
        val clothingItems = Seq.range(1, 5).map({ i => ClothingItemRow(Some(i), Random.nextString(10), Instant.EPOCH, Instant.EPOCH) })
        val clothingItemViewRows = clothingItems.map { item =>
          ClothingItemViewRow(item, categories, outfits)
        }

        (dataService.getClothingListView _)
          .expects()
          .returns(Future.successful(clothingItemViewRows))

        val fakeRequest = FakeRequest(routes.ClothingController.listClothingItems)
        val expectedResponse = clothingItemViewRows.map(_.transformInto[ClothingItemView])

        inside(route(app, fakeRequest)) {
          case Some(result) =>
            status(result) shouldEqual OK
            inside(contentAsJson(result).validate[Seq[ClothingItem]]) {
              case JsSuccess(response, _) =>
                response shouldBe expectedResponse
            }
        }
      }
    }
  }
}
