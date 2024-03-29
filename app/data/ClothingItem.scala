package data

import java.time.Instant

import slick.lifted.TableQuery
import slick.lifted.Tag
import CustomPostgresProfile.api._
import models.ClothingItemId
import models.ClothingItemName

case class ClothingItemRow(id: ClothingItemId, name: ClothingItemName, uploadedAt: Instant, updatedAt: Instant)

class ClothingItemTable(tag: Tag) extends Table[ClothingItemRow](tag, Some("public"), "clothing_item") {
  def id         = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name")
  def uploadedAt = column[Instant]("uploaded_at")
  def updatedAt  = column[Instant]("updated_at")

  override def * = (id, name, uploadedAt, updatedAt).mapTo[ClothingItemRow]
}

trait ClothingItemRepository {
  object clothingItem {
    val table = TableQuery[ClothingItemTable]

    object queries {
      def byName(name: ClothingItemName) =
        table.filter(_.name like s"%$name%").result
    }

    object actions {
      def insertOrUpdate(clothingItemName: ClothingItemName) =
        table.map(_.name).returning(table.map(_.id)) += clothingItemName
    }
  }
}
