package models

import java.util.UUID
import play.api.libs.json._

case class Ride(
                   id: UUID,
                   status: String = "requested",
                   petssenger: Petssenger,
                   driver: Driver,
                   distance: Int = 0,
                   price: Int = 0
                 )

object Ride {
  implicit val rideFormat: OFormat[Ride] = Json.format[Ride]
}