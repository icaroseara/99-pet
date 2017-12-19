package test

import javax.inject.Inject

import models.Petssenger
import repositories.PetssengerRepository
import org.scalatest.BeforeAndAfter
import play.api.libs.json.Json
import play.api.test.Helpers._
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PetssengerRepositorySpec @Inject()(petssengerRepository: PetssengerRepository) extends PlayWithMongoSpec with BeforeAndAfter {
  "PetssengerRepository respository" should {
    var petssengers: Future[JSONCollection] = null.asInstanceOf[Future[JSONCollection]]

    before {
      //Init DB
      await {
        petssengers = reactiveMongoApi.database.map(_.collection("petssengers"))

        val petssenger = Petssenger(None, "Buda", "duda@email.com")

        petssengers.flatMap(_.bulkInsert(ordered = false)(
          petssenger
        ))
      }
    }

    after {
      //clean DB
      petssengers.flatMap(_.drop(failIfNotFound = false))
    }


    "find a petssenger" in {
      val query = BSONDocument()
      val Some(petssengerToFind) = await(petssengers.flatMap(_.find(query).one[Petssenger]))
      val pessengerIdToFind = petssengerToFind.id.get
      petssengerRepository.find(pessengerIdToFind) mustBe Petssenger
    }
    "create a petseenger" in {

    }
    "update a petssenger" in {
      val query = BSONDocument()
      val Some(petssengerToUpdate) = await(petssengers.flatMap(_.find(query).one[Petssenger]))
      val petssenger = Petssenger(None, petssengerToUpdate.name , email= "pet@email.com")
      val petssengerIdToUpdate = petssengerToUpdate.id.get
      val newEmail = await(petssengerRepository.update(petssengerIdToUpdate, petssenger)).map(_.email)
      newEmail mustEqual "pet@new_email.com"
    }
  }
}