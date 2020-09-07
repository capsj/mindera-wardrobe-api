package models.api

import java.time.Instant

import play.api.libs.json.Format
import play.api.libs.json.Json
import play.api.libs.json.OFormat

case class ClothingItem(id: Option[Int], name: String, uploadedAt: Instant, updatedAt: Instant)
object ClothingItem {
  implicit val format: Format[ClothingItem] = Json.format
}

case class SearchClothingItemResponse(items: Seq[ClothingItem])
object SearchClothingItemResponse {
  implicit val format: OFormat[SearchClothingItemResponse] = Json.format
}
