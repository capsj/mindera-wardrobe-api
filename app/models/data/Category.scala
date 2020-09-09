package models.data

import slick.lifted.TableQuery
import slick.lifted.Tag
import CustomPostgresProfile.api._
import models.CategoryId
import models.CategoryName
import models.ClothingItemId

case class CategoryRow(id: CategoryId, name: CategoryName)
case class ClothingItemCategoryRow(id: Int, clothingItemId: Int, categoryId: Int)

class CategoryTable(tag: Tag) extends Table[CategoryRow](tag, Some("public"), "category") {
  def id   = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  override def * = (id, name).mapTo[CategoryRow]
}

class ClothingItemCategoryTable(tag: Tag) extends Table[ClothingItemCategoryRow](tag, Some("public"), "clothing_item_category") {
  def id             = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def clothingItemId = column[Int]("clothing_item_id")
  def categoryId     = column[Int]("category_id")

  override def * = (id, clothingItemId, categoryId).mapTo[ClothingItemCategoryRow]
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
      def insertOrUpdate(categoryName: CategoryName) =
        table.map(_.name).returning(table.map(_.id)) += categoryName

      def insertOrUpdate(clothingItemId: ClothingItemId, categoryId: CategoryId) =
        clothingItemCategoryTable.map(r => (r.clothingItemId, r.categoryId)) += (clothingItemId, categoryId)
    }
  }
}
