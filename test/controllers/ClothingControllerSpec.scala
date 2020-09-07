package controllers

import java.time.Instant

import scala.concurrent.Future

import io.scalaland.chimney.dsl._
import models.api.ClothingItem
import models.data.ClothingItemRow
import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status.OK
import play.api.libs.json.JsSuccess
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ClothingControllerSpec extends ControllerSpec with ScalaFutures {

  "ClothingController" - {
    "searchByName" - {
      "should respond with a list of clothing items" in {
        val term: String = scala.util.Random.nextString(10)
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
  }
}
