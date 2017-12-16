package controllers

import javax.inject.Inject

import play.api.libs.json._
import play.api.mvc._

class HealthController @Inject()(cc:ControllerComponents) extends AbstractController(cc)  {

  def isHealthy = Action {
    Ok(Json.obj("status" -> "OK", "message" -> "The application is healthy." ))
  }
}