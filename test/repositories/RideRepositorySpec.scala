package test

import javax.inject.Inject

import models.{Ride, Driver, Petssenger, Checkpoint}
import repositories.RideRepository
import org.scalatest.BeforeAndAfter
import play.api.libs.json.Json
import play.api.test.Helpers._
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RideRepositorySpec @Inject()(rideRepository: RideRepository) extends PlayWithMongoSpec with BeforeAndAfter {
  "RideRepository respository" should {
    var rides: Future[JSONCollection] = null.asInstanceOf[Future[JSONCollection]]

    before {
      //Init DB
      await {
        rides = reactiveMongoApi.database.map(_.collection("rides"))

        val petssenger = Petssenger(None, "Buda", "duda@email.com")
        val driver = Driver(None, "Paulo", "paulo@email.com", "fusca", "ANO2017")
        val ride = Ride(id = None, driver = driver, petssenger = petssenger)

        rides.flatMap(_.bulkInsert(ordered = false)(
          ride
        ))
      }
    }

    after {
      //clean DB
      rides.flatMap(_.drop(failIfNotFound = false))
    }

    "start a ride" in {
      val petssenger = Petssenger(None, "Caju", "caju@email.com")
      val driver = Driver(None, "Roberto", "roberto@email.com", "fusca", "ANO2017")
      val ride = Ride(id = None,driver = driver, petssenger = petssenger)
      val newRide = rideRepository.start(ride)
      newRide mustBe Ride
    }

    "find a ride" in {
      val query = BSONDocument()
      val Some(rideToFind) = await(rides.flatMap(_.find(query).one[Ride]))
      val rideIdToFind = rideToFind.id.get
      rideRepository.find(rideIdToFind) mustBe Ride
    }

    "update ride status" in {
      val query = BSONDocument()
      val Some(rideToUpdate) = await(rides.flatMap(_.find(query).one[Ride]))
      val checkpoint = Checkpoint(lat = -23.563210, lon = -46.654250)
      val checkpoints = checkpoint :: rideToUpdate.checkpoints
      val ride = Ride(None,
        driver = rideToUpdate.driver,
        petssenger = rideToUpdate.petssenger,
        checkpoints = rideToUpdate.checkpoints
      )
      val rideIdToUpdate = rideToUpdate.id.get
      val newCheckpoints = await(rideRepository.status(rideIdToUpdate, checkpoint)).map(_.checkpoints)
      newCheckpoints mustEqual checkpoints
    }
  }
}