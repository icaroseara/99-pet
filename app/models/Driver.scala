package models

import java.util.UUID
import play.api.libs.json._

case class Driver(
  id: UUID,
  name: String,
  email: String,
  carType: String,
  licensePlate: String
)

object Driver {
  implicit val driverFormat: OFormat[Driver] = Json.format[Driver]
}