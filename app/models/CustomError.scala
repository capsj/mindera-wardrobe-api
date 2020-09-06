package models

import play.api.libs.json.Format
import play.api.libs.json.Json
import play.api.mvc.Results.Status

trait CustomError extends Throwable {
  def message: String
  def defaultHttpStatus: Status
}

case class ErrorResponseBody(requestId: Long, message: String)
object ErrorResponseBody {
  implicit val format: Format[ErrorResponseBody] = Json.format
}
