package controllers.v1

import javax.inject.Inject

import play.api.mvc._
import play.api.Logger
import play.api.libs.json.Json
import services.DriverService
import models.Driver
import helpers.MongoExceptionHandler
import reactivemongo.bson.BSONObjectID


import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.Future

class DriverController @Inject()(cc: ControllerComponents, driverService: DriverService)
  extends AbstractController(cc)
  with MongoExceptionHandler  {

  private val logger = Logger(this.getClass)

  def create = Action.async(parse.json) { request =>
    request.body.validate[Driver].map{ driver =>
      driverService.create(driver).map{ _ =>
        Created
      }
    }.getOrElse(Future.successful(BadRequest("Invalid driver format")))
  }

  def read(id: BSONObjectID) = Action.async{ req =>
    driverService.find(id).map{ maybeDriver =>
      maybeDriver.map{ driver =>
        Ok(Json.toJson(driver))
      }.getOrElse(NotFound)
    }
  }

  def update(id: BSONObjectID) = TODO
}
