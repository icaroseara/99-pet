package test

import javax.inject.Inject

import models.Driver
import repositories.DriverRepository
import org.scalatest.BeforeAndAfter
import play.api.libs.json.Json
import play.api.test.Helpers._
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DriverRepositorySpec @Inject()(driverRepository: DriverRepository) extends PlayWithMongoSpec with BeforeAndAfter {
  "DriverRepository respository" should {
    var drivers: Future[JSONCollection] = null.asInstanceOf[Future[JSONCollection]]

    before {
      //Init DB
      await {
        drivers = reactiveMongoApi.database.map(_.collection("drivers"))

        val driver = Driver(None, "Paulo", "paulo@email.com", "fusca", "ANO2017")

        drivers.flatMap(_.bulkInsert(ordered = false)(
          driver
        ))
      }
    }

    after {
      //clean DB
      drivers.flatMap(_.drop(failIfNotFound = false))
    }


    "find a driver" in {
      val query = BSONDocument()
      val Some(driverToFind) = await(drivers.flatMap(_.find(query).one[Driver]))
      val pessengerIdToFind = driverToFind.id.get
      driverRepository.find(pessengerIdToFind) mustBe Driver
    }
    "create a driver" in {
      val driver = Driver(None, "Roberto", "roberto@email.com", "corsa", "ANO2015")
      val newDriver = driverRepository.create(driver)
      newDriver mustBe Driver
    }
    "update a driver" in {
      val query = BSONDocument()
      val Some(driverToUpdate) = await(drivers.flatMap(_.find(query).one[Driver]))
      val driver = Driver(None,
        driverToUpdate.name,
        email= driverToUpdate.email,
        driverToUpdate.carType,
        "ANO2018"
      )
      val driverIdToUpdate = driverToUpdate.id.get
      val newLicensePlate = await(driverRepository.update(driverIdToUpdate, driver)).map(_.licensePlate)
      newLicensePlate mustEqual "ANO2018"
    }
  }
}