package repositories

import javax.inject.Inject

import models.{Ride, Checkpoint}
import models.Ride._

import com.google.inject.ImplementedBy
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[RideRepositoryImpl])
trait RideRepository {
  def find(id: BSONObjectID): Future[Option[Ride]]

  def start(ride: Ride): Future[WriteResult]

  def status(id: BSONObjectID, checkpoint: Checkpoint): Future[Option[Ride]]

  def notification(id: BSONObjectID): Future[Option[Ride]]
}

class RideRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
  extends RideRepository {

  def ridesFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("rides"))

  override def find(id: BSONObjectID): Future[Option[Ride]] = {
    val query = BSONDocument("_id" -> id)
    ridesFuture.flatMap(_.find(query).one[Ride])
  }

  override def start(ride: Ride): Future[WriteResult] = {
    ridesFuture.flatMap(_.insert(ride))
  }

  override def status(id: BSONObjectID, checkpoint: Checkpoint): Future[Option[Ride]] = {
    val selector = BSONDocument("_id" -> id)

    val updateModifier = BSONDocument(
      "$set" -> BSONDocument(
        "checkpoints" -> "checkpoint"
      )
    )

    ridesFuture.flatMap(
      _.findAndUpdate(selector, updateModifier, fetchNewObject = true).map(_.result[Ride])
    )
  }

  override def notification(id: BSONObjectID): Future[Option[Ride]] = {
    val selector = BSONDocument("_id" -> id)
    val updateModifier = BSONDocument(
      "$set" -> BSONDocument(
        "status" -> "notification_sent"
      )
    )

    ridesFuture.flatMap(
      _.findAndUpdate(selector, updateModifier, fetchNewObject = true).map(_.result[Ride])
    )
  }
}