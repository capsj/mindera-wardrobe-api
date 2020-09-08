package models.data

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.Done
import cats.data.OptionT
import cats.instances.future._

trait DataService {
  def insertClothingItem(clothingItemRow: ClothingItemRow, categoryRow: CategoryRow): Future[Done]
  def searchByName(name: String): Future[Seq[ClothingItemRow]]

  def tagClothingItem(clothingItemId: Int, outfitId: Int): Future[Done]
  def getClothingListView: Future[Seq[ClothingItemViewRow]]
}

class DataServiceImpl(
  dataRepository: DataRepository
)(implicit executionContext: ExecutionContext)
  extends DataService {

  lazy val db = dataRepository.database

  override def insertClothingItem(clothingItemRow: ClothingItemRow, categoryRow: CategoryRow): Future[Done] =
    (for {
      clothingItem   <- OptionT(db.run(dataRepository.clothingItem.actions.insertOrUpdate(clothingItemRow)))
      category       <- OptionT(db.run(dataRepository.category.actions.insertOrUpdate(categoryRow)))
      clothingItemId <- OptionT.fromOption[Future](clothingItem.id)
      categoryId     <- OptionT.fromOption[Future](category.id)
      _              <- OptionT.liftF(db.run(dataRepository.category.actions.insertOrUpdate(ClothingItemCategoryRow(None, clothingItemId, categoryId))))
    } yield Done)
      .fold(Done)(identity)

  override def searchByName(name: String): Future[Seq[ClothingItemRow]] =
    db.run(dataRepository.clothingItem.queries.byName(name))

  override def tagClothingItem(clothingItemId: Int, outfitId: Int): Future[Done] =
    db.run(dataRepository.outfit.actions.tagClothingItem(clothingItemId, outfitId))
      .map(_ => Done)

  override def getClothingListView: Future[Seq[ClothingItemViewRow]] =
    db.run(dataRepository.clothingItemView.queries.list)
}
