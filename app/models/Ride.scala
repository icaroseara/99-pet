package models

import java.util.UUID
import play.api.libs.json._

case class Ride(
                   id: UUID,
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