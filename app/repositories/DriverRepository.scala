package repositories

import javax.inject.Inject

import models.Driver
import models.Driver._

import com.google.inject.ImplementedBy
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[DriverRepositoryImpl])
trait DriverRepository {
  def find(id: BSONObjectID): Future[Option[Driver]]

  def create(driver: Driver): Future[WriteResult]

  def update(id: BSONObjectID, driver: Driver): Future[Option[Driver]]
}

class DriverRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
  extends DriverRepository {

  def driversFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("drivers"))

  override def find(id: BSONObjectID): Future[Option[Driver]] = {
    val query = BSONDocument("_id" -> id)
    driversFuture.flatMap(_.find(query).one[Driver])
  }

  override def create(driver: Driver): Future[WriteResult] = {
    driversFuture.flatMap(_.insert(driver))
  }

  override def update(id: BSONObjectID, driver: Driver): Future[Option[Driver]] = {
    val selector = BSONDocument("_id" -> id)
    val updateModifier = BSONDocument(
      "$set" -> BSONDocument(
        "email" -> driver.email,
        "carType" -> driver.carType,
        "licensePlate" -> driver.licensePlate
      )
    )

    driversFuture.flatMap(
      _.findAndUpdate(selector, updateModifier, fetchNewObject = true).map(_.result[Driver])
    )
  }
}