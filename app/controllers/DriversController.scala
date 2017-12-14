package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class DriversController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index = TODO

  def create = TODO

  def read(id: String) = TODO

  def update(id: String) = TODO

  def delete(id: String) = TODO
}
