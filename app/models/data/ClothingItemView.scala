package models.data

import java.time.Instant

import slick.lifted.TableQuery
import slick.lifted.Tag
import CustomPostgresProfile.api._

case class ClothingItemViewRow(clothingItem: ClothingItemRow, categories: Seq[CategoryRow], outfits: Seq[OutfitRow])

class ClothingItemViewTable(tag: Tag) extends Table[ClothingItemViewRow](tag, Some("public"), "vw_clothing_item_details") {
  import CustomPostgresProfile.api._
  def id         = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name")
  def uploadedAt = column[Instant]("uploaded_at")
  def updatedAt  = column[Instant]("updated_at")
  def categories = column[Seq[CategoryRow]]("categories")
  def outfits    = column[Seq[OutfitRow]]("outfits")

  override def * =
    (id, name, uploadedAt, updatedAt, categories, outfits).shaped <> ({
      case (id, name, uploadedAt, updatedAt, categories, outfits) =>
        ClothingItemViewRow(
          ClothingItemRow(id, name, uploadedAt, updatedAt),
          categories,
          outfits
        )
    }, { row: ClothingItemViewRow =>
      Some(
        (
          row.clothingItem.id,
          row.clothingItem.name,
          row.clothingItem.uploadedAt,
          row.clothingItem.updatedAt,
          row.categories,
          row.outfits))
    })
}

trait ClothingItemViewRepository {
  object clothingItemView {
    val table = TableQuery[ClothingItemViewTable]

    object queries {
      def list = table.result
    }

  }
}
