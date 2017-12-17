package controllers.v1

import javax.inject.Inject

import helpers.MongoExceptionHandler
import play.api.Logger
import play.api.mvc._
import reactivemongo.bson.BSONObjectID

class RideController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc)
    with MongoExceptionHandler  {

  private val logger = Logger(this.getClass)

  def start = TODO

  def status(id: BSONObjectID) = TODO

  def notification(id: BSONObjectID) = TODO
}