package models

import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json._

case class Driver(
  id: Option[BSONObjectID],
  name: String,
  email: String,
  carType: String,
  licensePlate: String
)

object Driver {
  implicit val driverFormat: OFormat[Driver] = Json.format[Driver]
}