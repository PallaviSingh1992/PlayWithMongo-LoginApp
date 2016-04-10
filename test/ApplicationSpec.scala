import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {

    "BAD REQUEST SENDS ERROR 404" in  {
      route(app, FakeRequest(GET, "/invalid")).map(status(_)) mustBe Some(NOT_FOUND)
    }

  }

  "HomeController" should {

    "Display the LOGIN PAGE" in {
      val login = route(app, FakeRequest(GET, "/")).get
      status(login) mustBe OK
      contentType(login) mustBe Some("text/html")
      contentAsString(login) must include ("Login")
    }

    "Display the REGISTERATION PAGE" in {
      val reg = route(app, FakeRequest(POST, "/submitRegistration ")).get
      status(reg) mustBe OK
      contentType(reg) mustBe Some("text/html")
      contentAsString(reg) must include ("ID")
    }

  }

}
