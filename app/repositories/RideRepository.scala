package repositories

import javax.inject.Inject

import models.{Ride, Checkpoint, Driver, Petssenger}
import models.Ride._

import com.google.inject.ImplementedBy
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson._
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[RideRepositoryImpl])
trait RideRepository {
  def find(id: BSONObjectID): Future[Option[Ride]]

  def ongoing(driver: Driver, petssenger: Petssenger): List[Future[Option[Ride]]]

  def start(ride: Ride): Future[WriteResult]

  def status(id: BSONObjectID, checkpoint: Checkpoint): Future[Option[Ride]]
}

class RideRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
  extends RideRepository {

  def ridesFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("rides"))

  override def find(id: BSONObjectID): Future[Option[Ride]] = {
    val query = BSONDocument("id" -> id)
    ridesFuture.flatMap(_.find(query).one[Ride])
  }

  def ongoing(driver: Driver, petssenger: Petssenger): List[Future[Option[Ride]]] = {
    val petssengerQuery = BSONDocument(
      "petssenger" -> BSONDocument(
        "email" -> petssenger.email,
        "name" -> petssenger.name
      ),
      "status" -> BSONDocument(
        "$in" -> List("requested", "on_going")
      )
    )
    val petssengerRide = ridesFuture.flatMap(_.find(petssengerQuery).one[Ride])

    val driverQuery = BSONDocument(
      "driver" -> BSONDocument(
        "name" -> driver.name,
        "email" -> driver.email,
        "licensePlate" -> driver.licensePlate
      ),
      "status" -> BSONDocument(
        "$in" -> List("requested", "on_going")
      )
    )
    val driverRide = ridesFuture.flatMap(_.find(driverQuery).one[Ride])
    List(driverRide, petssengerRide)
  }


  override def start(ride: Ride): Future[WriteResult] = {
    ridesFuture.flatMap(_.insert(ride))
  }

  override def status(id: BSONObjectID, checkpoint: Checkpoint): Future[Option[Ride]] = {
    val selector = BSONDocument("id" -> id)
    val newCheckpoint = BSONDocument("lat" -> checkpoint.lat.signum, "lon" -> checkpoint.lon.signum)
    val updateModifier = BSONDocument("$push" -> BSONDocument("checkpoints" -> newCheckpoint))
    ridesFuture.flatMap(
      _.findAndUpdate(selector, updateModifier, fetchNewObject = true).map(_.result[Ride])
    )
  }
}