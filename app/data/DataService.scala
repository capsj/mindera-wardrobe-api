package data

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.Done
import models.CategoryName
import models.ClothingItemName
import slick.dbio.DBIOAction

trait DataService {
  def insertClothingItem(clothingItemName: ClothingItemName, categoryName: CategoryName): Future[Done]
  def searchByName(name: String): Future[Seq[ClothingItemRow]]
  def getClothingListView: Future[Seq[ClothingItemViewRow]]
  def tagClothingItem(clothingItemId: Int, outfitName: String): Future[Done]
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
        _              <- dataRepository.category.actions.insertOrUpdate(clothingItemId, categoryId)
      } yield Done
    }

  override def searchByName(name: String): Future[Seq[ClothingItemRow]] =
    db.run(dataRepository.clothingItem.queries.byName(name))

  override def tagClothingItem(clothingItemId: Int, outfitName: String): Future[Done] =
    db.run {
      for {
        outfitOpt <- dataRepository.outfit.queries.byName(outfitName)
        outfitId <-
          outfitOpt
            .map(o => DBIOAction.successful(o.id))
            .getOrElse(dataRepository.outfit.actions.insertOrUpdateOutfit(outfitName))
        _ <- dataRepository.outfit.actions.tagClothingItem(clothingItemId, outfitId)
      } yield Done

    }

  override def getClothingListView: Future[Seq[ClothingItemViewRow]] =
    db.run(dataRepository.clothingItemView.queries.list)
}
