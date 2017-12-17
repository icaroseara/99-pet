package models

import java.util.UUID
import play.api.libs.json._

case class Petssenger(
                   id: UUID,
                   name: String,
                   email: String
                 )

object Petssenger {
  implicit val petssengerFormat: OFormat[Petssenger] = Json.format[Petssenger]
}