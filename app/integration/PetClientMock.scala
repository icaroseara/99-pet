package integration

import javax.inject.Inject

import models.{Checkpoint, Ride}
import play.api.Logger
import play.api.libs.ws.WSClient
import play.api.libs.json.{Json, _}
import play.api.Environment

import scala.concurrent.{ExecutionContext, Future}

class PetClientMock(ws: WSClient, baseUrl: String)(implicit ec: ExecutionContext, val env: Environment) {
  @Inject def this(ws: WSClient, ec: ExecutionContext, env: Environment) = this(ws, "https://pet.99app.com")(ec, env)

  private val logger = Logger(this.getClass)
  private val errorValue = -1.0f

  def calculateDistance(checkpoints: List[Checkpoint]): Float = {
    logger.debug("Calculate distance")
    val response: Option[JsValue] = env.resourceAsStream("./response/distance.json") map ( Json.parse(_) )
    response match {
      case Some(m) => m("distance").as[Float]
      case _ => {
        logger.error("Failed to get distance")
        errorValue
      }
    }
  }

  def calculatePrice(distance: Float): Float = {
    logger.debug(s"Calculate price")
    val response: Option[JsValue] = env.resourceAsStream("./response/price.json") map ( Json.parse(_) )
    response match {
      case Some(m) => m("price").as[Float]
      case _ => {
        logger.error("Failed to get distance")
        errorValue
      }
    }
  }

  def sendEmail(ride: Ride, distance: Float, price: Float): JsValue = {
    logger.debug(s"Send email")
    val response: Option[JsValue] = env.resourceAsStream("./response/email.json") map ( Json.parse(_) )
    response match {
      case Some(e) => e
      case _ => {
        logger.error("Failed to get distance")
        Json.obj()
      }
    }
  }

  def sendNotification(ride: Ride): JsValue = {
    logger.debug("Send notification")
    val distance = calculateDistance(ride.checkpoints)
    val price = calculatePrice(distance)
    sendEmail(ride, price, distance)
  }
}

