package models.data

import slick.jdbc.JdbcProfile

trait DataRepository
  extends ClothingItemRepository
    with OutfitRepository
    with ClothingItemViewRepository
    with CategoryRepository {
  def database: JdbcProfile#Backend#Database
}
