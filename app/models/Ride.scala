package models

import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json._

case class Ride(
                 id: Option[BSONObjectID],
                 status: String = "requested",
                 petssenger: Petssenger,
                 driver: Driver,
                 distance: Float = 0,
                 price: Float = 0,
                 checkpoints: List[Checkpoint] = List[Checkpoint]()
               )

object Ride {
  implicit val rideFormat: OFormat[Ride] = Json.format[Ride]
}