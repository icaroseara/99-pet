package services

import javax.inject.Inject

import repositories.PetssengerRepository
import models.Petssenger

import com.google.inject.ImplementedBy
import reactivemongo.bson.BSONObjectID
import reactivemongo.api.commands.WriteResult
import scala.concurrent.Future

@ImplementedBy(classOf[PetssengerServiceImpl])
trait PetssengerService {
  def find(id: BSONObjectID): Future[Option[Petssenger]]

  def create(petssenger: Petssenger): Future[WriteResult]

  def update(id: BSONObjectID, petssenger: Petssenger): Future[Option[Petssenger]]
}

class PetssengerServiceImpl @Inject()(petssengerRepository: PetssengerRepository) extends PetssengerService {
  override def find(id: BSONObjectID): Future[Option[Petssenger]] =
    petssengerRepository.find(id)

  override def create(petssenger: Petssenger): Future[WriteResult] =
    petssengerRepository.create(petssenger)

  override def update(id: BSONObjectID, petssenger: Petssenger): Future[Option[Petssenger]] =
    petssengerRepository.update(id, petssenger)
}