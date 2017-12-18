package services

import javax.inject.Inject

import repositories.RideRepository
import models.{Ride, Checkpoint}

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

class RideServiceImpl @Inject()(rideRepository: RideRepository) extends RideService {

  override def start(ride: Ride): Future[WriteResult] =
    rideRepository.start(ride)

  override def status(id: BSONObjectID, checkpoint: Checkpoint): Future[Option[Ride]] =
    rideRepository.status(id, checkpoint)

  override def notification(id: BSONObjectID): Future[Option[Ride]] =
    rideRepository.notification(id)
}