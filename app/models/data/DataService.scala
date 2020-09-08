package models.data

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.Done
import models.CategoryName
import models.ClothingItemName
import play.api.Logging

trait DataService {
  def insertClothingItem(clothingItemName: ClothingItemName, categoryName: CategoryName): Future[Done]
  def searchByName(name: String): Future[Seq[ClothingItemRow]]

  def tagClothingItem(clothingItemId: Int, outfitId: Int): Future[Done]
  def getClothingListView: Future[Seq[ClothingItemViewRow]]
}

class DataServiceImpl(
  dataRepository: DataRepository
)(implicit executionContext: ExecutionContext)
  extends DataService {
  lazy val db = dataRepository.database

  override def insertClothingItem(clothingItemName: ClothingItemName, categoryName: CategoryName): Future[Done] =
    db.run {
      for {
        clothingItemId <- dataRepository.clothingItem.actions.insertOrUpdate(clothingItemName)
        categoryId     <- dataRepository.category.actions.insertOrUpdate(categoryName)
        _              <- dataRepository.category.actions.insertOrUpdate(ClothingItemCategoryRow(None, clothingItemId, categoryId))
      } yield Done
    }

  override def searchByName(name: String): Future[Seq[ClothingItemRow]] =
    db.run(dataRepository.clothingItem.queries.byName(name))

  override def tagClothingItem(clothingItemId: Int, outfitId: Int): Future[Done] =
    db.run(dataRepository.outfit.actions.tagClothingItem(clothingItemId, outfitId))
      .map(_ => Done)

  override def getClothingListView: Future[Seq[ClothingItemViewRow]] =
    db.run(dataRepository.clothingItemView.queries.list)
}
