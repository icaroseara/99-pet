package repositories

import javax.inject.Inject

import models.Petssenger
import models.Petssenger._

import com.google.inject.ImplementedBy
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[PetssengerRepositoryImpl])
trait PetssengerRepository {
  def find(id: BSONObjectID): Future[Option[Petssenger]]

  def create(petssenger: Petssenger): Future[WriteResult]

  def update(id: BSONObjectID, petssenger: Petssenger): Future[Option[Petssenger]]
}

class PetssengerRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
  extends PetssengerRepository {

  def petssengersFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("petssengers"))

  override def find(id: BSONObjectID): Future[Option[Petssenger]] = {
    val query = BSONDocument("id" -> id)
    petssengersFuture.flatMap(_.find(query).one[Petssenger])
  }

  override def create(petssenger: Petssenger): Future[WriteResult] = {
    petssengersFuture.flatMap(_.insert(petssenger))
  }

  override def update(id: BSONObjectID, petssenger: Petssenger): Future[Option[Petssenger]] = {
    val selector = BSONDocument("id" -> id)
    val updateModifier = BSONDocument(
      "$set" -> BSONDocument(
        "email" -> petssenger.email
      )
    )

    petssengersFuture.flatMap(
      _.findAndUpdate(selector, updateModifier, fetchNewObject = true).map(_.result[Petssenger])
    )
  }
}