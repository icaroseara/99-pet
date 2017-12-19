package services

import javax.inject.Inject

import repositories.RideRepository
import models.{Ride, Checkpoint}
import integration.PetClientMock

import scala.concurrent.ExecutionContext.Implicits.global
import com.google.inject.ImplementedBy
import reactivemongo.bson.BSONObjectID
import reactivemongo.api.commands.WriteResult
import scala.concurrent.Future


@ImplementedBy(classOf[RideServiceImpl])
trait RideService {

  def start(ride: Ride): Future[WriteResult]

  def status(id: BSONObjectID, checkpoint: Checkpoint): Future[Option[Ride]]

  def notification(id: BSONObjectID): Future[Option[Ride]]
}

class RideServiceImpl @Inject()(rideRepository: RideRepository, petClient: PetClientMock) extends RideService {

  override def start(ride: Ride): Future[WriteResult] =
    rideRepository.start(ride)

  override def status(id: BSONObjectID, checkpoint: Checkpoint): Future[Option[Ride]] =
    rideRepository.status(id, checkpoint)

  override def notification(id: BSONObjectID): Future[Option[Ride]] = {
    val futureRide = rideRepository.find(id)
    futureRide.map{ maybeRide =>
      maybeRide.map{ ride =>
        petClient.sendNotification(ride)
      }
    }
    futureRide
  }
}