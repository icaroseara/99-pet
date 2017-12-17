package controllers.v1

import javax.inject.Inject

import play.api.mvc._
import play.api.Logger
import play.api.libs.json.Json

import helpers.MongoExceptionHandler
import services.RideService
import models.Ride

import scala.concurrent._
import reactivemongo.bson.BSONObjectID
import ExecutionContext.Implicits.global

import scala.concurrent.Future

class RideController @Inject()(cc: ControllerComponents, rideService: RideService)
  extends AbstractController(cc)
    with MongoExceptionHandler  {

  private val logger = Logger(this.getClass)

  def start = Action.async(parse.json) { request =>
    request.body.validate[Ride].map{ ride =>
      rideService.start(ride).map{ _ =>
        Created("Ride started")
      }
    }.getOrElse{
      logger.info(s"Cannot create a ride with invalid info")
      Future.successful(BadRequest("Invalid ride format"))
    }
  }

  def status(id: BSONObjectID) =  Action.async(parse.json){ req =>
    req.body.validate[Ride].map{ ride =>
      rideService.status(id).map {
        case Some(ride) => Ok(Json.toJson(ride))
        case None => NotFound
      }
    }.getOrElse{
      logger.info(s"Cannot update a ride status within invalid info")
      Future.successful(BadRequest("Invalid request body"))
    }
  }

  def notification(id: BSONObjectID) = Action.async{ _ =>
    rideService.notification(id).map{ maybeRide =>
      maybeRide.map{ _ =>
        Ok("Ride notification sent")
      }.getOrElse{
        logger.info(s"Cannot send notification with a ride ID: $id")
        NotFound(s"Cannot send notification with a ride ID: $id")
      }
    }
  }
}