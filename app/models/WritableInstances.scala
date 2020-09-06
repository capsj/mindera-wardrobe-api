package models

import play.api.http.Writeable
import play.api.libs.json.Format
import play.api.libs.json.Json
import play.api.libs.json.JsValue

object WritableInstances extends WritableInstances
trait WritableInstances {
  implicit def WritableJson[A: Format]: Writeable[A] = implicitly[Writeable[JsValue]].map(Json.toJson(_))
}
