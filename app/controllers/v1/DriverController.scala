package controllers.v1

import javax.inject.Inject

import play.api.mvc._
import play.api.Logger
import play.api.libs.json.Json

import helpers.MongoExceptionHandler
import services.DriverService
import models.Driver

import scala.concurrent._
import reactivemongo.bson.BSONObjectID
import ExecutionContext.Implicits.global

class DriverController @Inject()(cc: ControllerComponents, driverService: DriverService)
  extends AbstractController(cc)
  with MongoExceptionHandler  {

  private val logger = Logger(this.getClass)

  def create = Action.async(parse.json) { request =>
    request.body.validate[Driver].map{ driver =>
      driverService.create(driver).map{ _ =>
        Created
      }
    }.getOrElse{
      logger.info(s"Cannot create a driver with invalid info")
      Future.successful(BadRequest("Invalid driver format"))
    }
  }

  def read(id: BSONObjectID) = Action.async{ req =>
    driverService.find(id).map{ maybeDriver =>
      maybeDriver.map{ driver =>
        Ok(Json.toJson(driver))
      }.getOrElse{
        logger.info(s"Cannot find a driver with: id=$id")
        NotFound(s"Cannot find a user with id=$id")
      }
    }
  }

  def update(id: BSONObjectID) =  Action.async(parse.json){ req =>
    req.body.validate[Driver].map{ driver =>
      driverService.update(id, driver).map {
        case Some(driver) => Ok(Json.toJson(driver))
        case None => NotFound
      }
    }.getOrElse{
      logger.info(s"Cannot update a driver invalid info")
      Future.successful(BadRequest("Invalid request body"))
    }
  }
}
