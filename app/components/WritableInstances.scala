package components

import play.api.http.Writeable._
import play.api.http._
import play.api.libs.json._

object WritableInstances extends WritableInstances
trait WritableInstances {
  implicit def WritableJson[A: Format]: Writeable[A] = implicitly[Writeable[JsValue]].map(Json.toJson(_))
}
