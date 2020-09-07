package models.data

import java.time.Instant

import slick.lifted.TableQuery
import slick.lifted.Tag
import CustomPostgresProfile.api._

case class ClothingItemRow(id: Option[Int], name: String, uploadedAt: Instant, updatedAt: Instant)

class ClothingItemTable(tag: Tag) extends Table[ClothingItemRow](tag, "clothing_item") {
  import CustomPostgresProfile.api._
  def id         = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name")
  def uploadedAt = column[Instant]("uploaded_at")
  def updatedAt  = column[Instant]("updated_at")

  override def * = (id.?, name, uploadedAt, updatedAt).mapTo[ClothingItemRow]
}

trait ClothingItemRepository {
  object clothingItem {
    val table = TableQuery[ClothingItemTable]

    object queries {
      def byName(name: String) =
        table.filter(row => row.name.like(name)).result
    }

    object actions {
      def insertOrUpdate(name: String) =
        table.returning(table).insertOrUpdate(ClothingItemRow(None, name, Instant.now(), Instant.now()))
    }
  }
}
