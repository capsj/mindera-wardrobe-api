package models.data

import slick.lifted.TableQuery
import slick.lifted.Tag
import CustomPostgresProfile.api._

case class OutfitRow(id: Int, name: String)
case class ClothingItemOutfitRow(id: Int, clothingItemId: Int, outfitId: Int)

class OutfitTable(tag: Tag) extends Table[OutfitRow](tag, Some("public"), "outfit") {
  def id   = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  override def * = (id, name).mapTo[OutfitRow]
}

class ClothingItemOutfitTable(tag: Tag) extends Table[ClothingItemOutfitRow](tag, Some("public"), "clothing_item_outfit") {
  def id             = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def clothingItemId = column[Int]("clothing_item_id")
  def outfitId       = column[Int]("outfit_id")

  override def * = (id, clothingItemId, outfitId).mapTo[ClothingItemOutfitRow]
}

trait OutfitRepository {
  object outfit {
    val outfitTable         = TableQuery[OutfitTable]
    val clothingOutfitTable = TableQuery[ClothingItemOutfitTable]

    object queries {
      def byName(name: String) =
        outfitTable.filter(_.name === name).result.headOption
    }

    object actions {
      def insertOrUpdateOutfit(name: String) =
        outfitTable.map(_.name).returning(outfitTable.map(_.id)) += name

      def tagClothingItem(clothingItemId: Int, outfitId: Int) =
        clothingOutfitTable.map(r => (r.clothingItemId, r.outfitId)) += (clothingItemId, outfitId)
    }
  }
}
