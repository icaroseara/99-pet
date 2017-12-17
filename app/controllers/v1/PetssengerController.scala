package controllers.v1

import javax.inject.Inject

import play.api.mvc._
import play.api.Logger
import play.api.libs.json.Json

import helpers.MongoExceptionHandler
import services.PetssengerService
import models.Petssenger

import scala.concurrent._
import reactivemongo.bson.BSONObjectID
import ExecutionContext.Implicits.global

class PetssengerController @Inject()(cc: ControllerComponents, petssengerService: PetssengerService)
  extends AbstractController(cc)
    with MongoExceptionHandler  {

  private val logger = Logger(this.getClass)

  def create = Action.async(parse.json) { request =>
    request.body.validate[Petssenger].map{ petssenger =>
      petssengerService.create(petssenger).map{ _ =>
        Created
      }
    }.getOrElse{
      logger.info(s"Cannot create a petssenger with invalid info")
      Future.successful(BadRequest("Invalid petssenger format"))
    }
  }

  def read(id: BSONObjectID) = Action.async{ req =>
    petssengerService.find(id).map{ maybePetssenger =>
      maybePetssenger.map{ petssenger =>
        Ok(Json.toJson(petssenger))
      }.getOrElse{
        logger.info(s"Cannot find a petssenger with: id=$id")
        NotFound(s"Cannot find a user with id=$id")
      }
    }
  }

  def update(id: BSONObjectID) =  Action.async(parse.json){ req =>
    req.body.validate[Petssenger].map{ petssenger =>
      petssengerService.update(id, petssenger).map {
        case Some(petssenger) => Ok(Json.toJson(petssenger))
        case None => NotFound
      }
    }.getOrElse{
      logger.info(s"Cannot update a petssenger invalid info")
      Future.successful(BadRequest("Invalid request body"))
    }
  }
}
