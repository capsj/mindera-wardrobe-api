package models.data

import slick.jdbc.JdbcBackend.Database

trait DataRepository extends ClothingItemRepository with OutfitRepository with ClothingItemViewRepository with CategoryRepository {
  def database: Database
}
