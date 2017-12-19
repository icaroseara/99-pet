package models

import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json._

case class Petssenger(id: Option[BSONObjectID], name: String, email: String)

object Petssenger {
  implicit val petssengerFormat: OFormat[Petssenger] = Json.format[Petssenger]
}