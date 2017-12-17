package helpers

import repositories.exceptions.{MongoInsertException, MongoObjectNotFoundException, MongoUpdateException}
import play.api.libs.json.JsonValidationError
import play.api.libs.json._
import play.api.mvc.Result
import play.api.mvc.Results._

import scala.concurrent.{ExecutionContext, Future}

trait MongoExceptionHandler {
  def handleValidationErrors(implicit ctx: ExecutionContext): Seq[(JsPath, Seq[JsonValidationError])] => Future[Result] = {
    errors => Future {
      BadRequest(Json.obj("errors" -> s"Invalid json format: ${errors}" ))
    }
  }

  def handleUpdateFailure: PartialFunction[Throwable, Result] = {
    case e: MongoObjectNotFoundException => NotFound(e.getMessage)
    case e: MongoUpdateException => BadRequest(e.getMessage)
    case e: Exception => InternalServerError(e.getMessage)
  }

  def handleCreateFailure: PartialFunction[Throwable, Result] = {
    case e: MongoInsertException => BadRequest(e.getMessage)
    case e: Exception => InternalServerError(e.getMessage)
  }
}
