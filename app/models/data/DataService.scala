package models.data

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.Done

trait DataService {
  def insertClothingItem(name: String): Future[Option[ClothingItemRow]]
  def searchByName(name: String): Future[Seq[ClothingItemRow]]

  def tagClothingItem(clothingItemId: Int, outfitId: Int): Future[Done]
  def getClothingListView: Future[Seq[ClothingItemViewRow]]
}

class DataServiceImpl(
  dataRepository: DataRepository
)(implicit executionContext: ExecutionContext)
  extends DataService {

  lazy val db = dataRepository.database

  override def insertClothingItem(name: String): Future[Option[ClothingItemRow]] =
    db.run(dataRepository.clothingItem.actions.insertOrUpdate(name))

  override def searchByName(name: String): Future[Seq[ClothingItemRow]] =
    db.run(dataRepository.clothingItem.queries.byName(name))

  override def tagClothingItem(clothingItemId: Int, outfitId: Int): Future[Done] =
    db.run(dataRepository.outfit.actions.tagClothingItem(clothingItemId, outfitId))
      .map(_ => Done)

  override def getClothingListView: Future[Seq[ClothingItemViewRow]] =
    db.run(dataRepository.clothingItemView.queries.list)
}
