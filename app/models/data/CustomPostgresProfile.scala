package models.data

import com.github.tminglei.slickpg._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import slick.jdbc.JdbcCapabilities

object CustomPostgresProfile extends CustomPostgresProfile
trait CustomPostgresProfile
  extends ExPostgresProfile
  with PgEnumSupport
  with PgSearchSupport
  with PgArraySupport
  with PgDate2Support
  with PgPlayJsonSupport {

  /** For Postgres 9.4.0 and newer. */
  def pgjson = "jsonb"

  override val api = CustomApi

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  object CustomApi extends super.API with JsonImplicits {

    import slick.ast._
    import slick.ast.Library._

    implicit val checkinOptionIdListMapper: BaseColumnType[Seq[String]] =
      MappedColumnType.base[Seq[String], JsValue](Json.toJson(_), _.as[List[String]])

  }
}
