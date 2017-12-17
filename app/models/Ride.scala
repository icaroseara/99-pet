package models

import java.util.UUID

case class Ride(id: UUID, status: String, petssenger: Petssenger, driver: Driver, distance: Integer, price: Integer)