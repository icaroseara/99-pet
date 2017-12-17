package services

import javax.inject.Inject

import repositories.DriverRepository
import models.Driver

import com.google.inject.ImplementedBy
import reactivemongo.bson.BSONObjectID
import reactivemongo.api.commands.WriteResult
import scala.concurrent.Future

@ImplementedBy(classOf[DriverServiceImpl])
trait DriverService {
  def find(id: BSONObjectID): Future[Option[Driver]]

  def create(driver: Driver): Future[WriteResult]

  def update(id: BSONObjectID, driver: Driver): Future[Option[Driver]]
}

class DriverServiceImpl @Inject()(driverRepository: DriverRepository) extends DriverService {
  override def find(id: BSONObjectID): Future[Option[Driver]] =
    driverRepository.find(id)

  override def create(driver: Driver): Future[WriteResult] =
    driverRepository.create(driver)

  override def update(id: BSONObjectID, driver: Driver): Future[Option[Driver]] =
    driverRepository.update(id, driver)
}