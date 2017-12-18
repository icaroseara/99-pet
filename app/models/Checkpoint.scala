package models

import play.api.libs.json.{Json, OFormat}

case class Checkpoint(
                       lat: BigDecimal,
                       lon: BigDecimal
                     )

object Checkpoint {
  implicit val checkpointFormat: OFormat[Checkpoint] = Json.format[Checkpoint]
}