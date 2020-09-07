package models.data

import java.time.Instant

import slick.lifted.TableQuery
import slick.lifted.Tag
import CustomPostgresProfile.api._

case class CategoryRow(id: Option[Int], name: String)

class CategoryTable(tag: Tag) extends Table[CategoryRow](tag, "category") {
  import CustomPostgresProfile.api._
  def id         = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name       = column[String]("name")

  override def * = (id.?, name).mapTo[CategoryRow]
}

trait CategoryRepository {
  object category {
    val table = TableQuery[CategoryTable]

    object queries {
      def byName(name: String) =
        table.filter(row => row.name.like(name)).result
    }

    object actions {
      def insertOrUpdate(name: String) =
        table.insertOrUpdate(CategoryRow(None, name, Instant.now(), Instant.now()))
    }
  }
}
