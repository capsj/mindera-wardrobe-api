package models.data

import com.github.tminglei.slickpg._
import play.api.libs.json.Format
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import slick.jdbc.JdbcCapabilities

object CustomPostgresProfile extends CustomPostgresProfile
trait CustomPostgresProfile extends ExPostgresProfile with PgArraySupport with PgPlayJsonSupport {

  /** For Postgres 9.4.0 and newer. */
  def pgjson = "jsonb"

  override val api = CustomApi

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  object CustomApi extends super.API with ArrayImplicits with SimpleArrayPlainImplicits with JsonImplicits {
    implicit val categoryRowFormat: Format[CategoryRow] = Json.format
    implicit val outfitRowFormat: Format[OutfitRow]     = Json.format

    implicit val StringListMapper: BaseColumnType[Seq[String]] =
      MappedColumnType.base[Seq[String], JsValue](Json.toJson(_), _.as[List[String]])

    implicit val CategoryRowSeqMapper: BaseColumnType[Seq[CategoryRow]] =
      MappedColumnType.base[Seq[CategoryRow], JsValue](Json.toJson(_), _.as[List[CategoryRow]])

    implicit val OutfitRowSeqMapper: BaseColumnType[Seq[OutfitRow]] =
      MappedColumnType.base[Seq[OutfitRow], JsValue](Json.toJson(_), _.as[List[OutfitRow]])
  }
}
