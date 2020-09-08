package models.data

import java.time.Instant

import slick.lifted.TableQuery
import slick.lifted.Tag
import CustomPostgresProfile.api._
import play.api.libs.json.Format
import play.api.libs.json.Json

case class CategoryRow(id: Option[Int], name: String)
case class ClothingItemCategoryRow(id: Option[Int], clothingItemId: Int, categoryId: Int)

class CategoryTable(tag: Tag) extends Table[CategoryRow](tag, "category") {
  import CustomPostgresProfile.api._
  def id   = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  override def * = (id.?, name).mapTo[CategoryRow]
}

class ClothingItemCategoryTable(tag: Tag) extends Table[ClothingItemCategoryRow](tag, "clothing_item_category") {
  import CustomPostgresProfile.api._
  def id             = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def clothingItemId = column[Int]("clothing_item_id")
  def categoryId     = column[Int]("category_id")

  override def * = (id.?, clothingItemId, categoryId).mapTo[ClothingItemCategoryRow]
}

trait CategoryRepository {
  object category {
    val table = TableQuery[CategoryTable]
    val clothingItemCategoryTable = TableQuery[ClothingItemCategoryTable]

    object queries {
      def byName(name: String) =
        table.filter(row => row.name.like(name)).result
    }

    object actions {
      def insertOrUpdate(categoryRow: CategoryRow) =
        table.returning(table).insertOrUpdate(categoryRow)

      def insertOrUpdate(clothingItemCategoryRow: ClothingItemCategoryRow) =
        clothingItemCategoryTable.insertOrUpdate(clothingItemCategoryRow)
    }
  }
}
