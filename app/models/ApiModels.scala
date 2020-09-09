package models

import java.time.Instant

import play.api.libs.json.Format
import play.api.libs.json.Json

case class ClothingItem(id: Int, name: String, uploadedAt: Instant, updatedAt: Instant)
object ClothingItem {
  implicit val format: Format[ClothingItem] = Json.format
}

case class Category(id: Int, name: String)
object Category {
  implicit val format: Format[Category] = Json.format
}

case class Outfit(id: Int, name: String)
object Outfit {
  implicit val format: Format[Outfit] = Json.format
}

case class ClothingItemView(clothingItem: ClothingItem, categories: Seq[Category], outfits: Seq[Outfit])
object ClothingItemView {
  implicit val format: Format[ClothingItemView] = Json.format
}
