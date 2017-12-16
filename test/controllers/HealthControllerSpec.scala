package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

class HealthControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "HealthController GET" should {
    "return a healthy check state" in {
      val request = FakeRequest(GET, "/api/health")
      val health = route(app, request).get

      status(health) mustBe OK
      contentType(health) mustBe Some("application/json")
    }
  }
}
