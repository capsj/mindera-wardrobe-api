package models.data

import java.time.Instant

import slick.lifted.Query
import slick.lifted.TableQuery
import slick.lifted.Tag
import CustomPostgresProfile.api._

case class ClothingItemViewRow(clothingItemRow: ClothingItemRow, categories: Seq[String], outfits: Seq[String])

class ClothingItemViewTable(tag: Tag) extends Table[ClothingItemViewRow](tag, "clothing_item") {
  import CustomPostgresProfile.api._
  def id         = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name")
  def uploadedAt = column[Instant]("uploaded_at")
  def updatedAt  = column[Instant]("updated_at")
  def categories = column[Seq[String]]("categories")
  def outfits    = column[Seq[String]]("outfits")

  override def * =
    (id.?, name, uploadedAt, updatedAt, categories, outfits).shaped <> ({
      case (id, name, uploadedAt, updatedAt, categories, outfits) =>
        ClothingItemViewRow(
          ClothingItemRow(id, name, uploadedAt, updatedAt),
          categories,
          outfits
        )
    }, { row: ClothingItemViewRow =>
      import row.clothingItemRow
      Some(
        (
          row.clothingItemRow.id,
          row.clothingItemRow.name,
          row.clothingItemRow.uploadedAt,
          row.clothingItemRow.updatedAt,
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
