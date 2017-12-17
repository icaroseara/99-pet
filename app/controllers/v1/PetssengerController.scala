package controllers.v1

import javax.inject.Inject

import helpers.MongoExceptionHandler
import play.api.Logger
import play.api.mvc._
import reactivemongo.bson.BSONObjectID

class PetssengerController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc)
      with MongoExceptionHandler  {

  private val logger = Logger(this.getClass)

  def create = TODO

  def read(id: BSONObjectID) = TODO

  def update(id: BSONObjectID) = TODO
}