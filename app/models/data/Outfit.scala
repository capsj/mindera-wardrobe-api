package models.data

import slick.lifted.TableQuery
import slick.lifted.Tag
import CustomPostgresProfile.api._

case class OutfitRow(id: Option[Int], name: String)
case class ClothingItemOutfitRow(id: Option[Int], clothingItemId: Int, outfitId: Int)

class OutfitTable(tag: Tag) extends Table[OutfitRow](tag, "outfit") {
  import CustomPostgresProfile.api._
  def id   = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  override def * = (id.?, name).mapTo[OutfitRow]
}

class ClothingItemOutfitTable(tag: Tag) extends Table[ClothingItemOutfitRow](tag, "clothing_item_outfit") {
  import CustomPostgresProfile.api._
  def id             = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def clothingItemId = column[Int]("clothing_item_id")
  def outfitId       = column[Int]("outfit_id")

  override def * = (id.?, clothingItemId, outfitId).mapTo[ClothingItemOutfitRow]
}

trait OutfitRepository {
  object outfit {
    val outfitTable         = TableQuery[OutfitTable]
    val clothingOutfitTable = TableQuery[ClothingItemOutfitTable]

    object queries {
      def byId(id: Int) =
        outfitTable.filter(row => row.id === id).result.headOption
    }

    object actions {
      def insertOrUpdateOutfit(name: String) =
        outfitTable.insertOrUpdate(OutfitRow(None, name))

      def tagClothingItem(clothingItemId: Int, outfitId: Int) =
        clothingOutfitTable.insertOrUpdate(ClothingItemOutfitRow(None, clothingItemId, outfitId))
    }
  }
}
