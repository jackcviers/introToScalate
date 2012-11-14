package scalate

import unfiltered.filter.async.Plan
import unfiltered.request.{GET, Path}
import unfiltered.response.{HtmlContent, Ok}
import org.openqa.selenium.{By,WebElement}
import org.fusesource.scalate.test._
import org.junit.runner.RunWith
import org.openqa.selenium.WebElement
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.junit.Test
import scala.collection.JavaConversions._
import scalaz._
import scalaz.Scalaz._
import unfiltered.scalate.Scalate

@RunWith(classOf[JUnitRunner])
class ExampleIntegrationTest extends FunSuite with WebServerMixin with WebDriverMixin  {
  import unfiltered.jetty._
  lazy val port = unfiltered.util.Port.any
  lazy val server = setup(Http(port))
  def setup: (Server => Server) = (http) => http.filter(TestPlan)
  
  override protected def beforeAll(configMap: Map[String, Any]) = {
    System.setProperty("scalate.mode", "development")
    super.beforeAll(configMap)
    setup
    server.start()
    webServer.server = server.underlying
  }
  
  override protected def afterAll(configMap: Map[String, Any]) = {
    webServer.server = server.underlying
    server.stop()
    server.destroy()
    super.afterAll(configMap)
  }

  def findElements(xPathExpression: String)(implicit body:WebElement): 
  Either[List[Throwable], List[WebElement]] = { 
    val elements:List[WebElement] = body.findElements(By.xpath(xPathExpression)).toList
    try { 
      if(elements.length != 0) 
        Right(elements) 
      else 
        throw new IllegalStateException("Element Not Found for xpath expression: " + xPathExpression)
    } catch { 
      case error:Throwable => Left(List(error))
    }
  }
  def text(e: WebElement): String = e.getText()
  def elements(e: Either[List[Throwable], List[WebElement]]) = e
    .right
    .getOrElse { Nil }
  def texts(
    e: Either[List[Throwable], List[WebElement]])(
      f: Either[List[Throwable], List[WebElement]] => List[WebElement])(
        g: WebElement => String): List[String] = f(e) map g
  
  test("TestPlan") {
    lazy implicit val body = xpath("//body")

    lazy val slide1 = findElements("//div[@id='slide1']")
    lazy val email = findElements("//a[@href='mailto:jackcviers@gmail.com']")

    webDriver.get("http://localhost:"+port+"/")
    println(texts(email)(elements)(text))
    
    val valid = slide1 <*> email
    
    assert(valid.isRight, valid + "did not contain all the selected elements.")
    assert(elements(slide1).length == 1, "Too many title slides.")
    assert(elements(email).length == 1, "Too many email links.")
    assert(texts(email)(elements)(text).contains("jackcviers@gmail.com"), "The email address is wrong!")
  }
}

object TestPlan extends Plan with PlanShared with ScalateTestSlideShow {
    def intent = {
      case req@GET(Path("/")) ⇒
      val title = "Scalate: The write less do more scala templating engine."
      req.respond(Ok ~> HtmlContent ~> Scalate(req, baseDirectory + "index.jade", ("slides" → repository.read))(additionalAttributes = ("title" → title) :: Nil))
    }
}

class TestScalateInMemoryRepository extends SlideshowRepository[Slides] {
  def read = Slides(
    Seq(
      Title(1, "Scalate", "The write less do more Scala templating engine.", "Jack Viers", "jackcviers@gmail.com", "jackviers", ""),
      Headline(2, "Besides black art, there is only automation and mechanization.", "-- Frederico Garcia Lorca", "", "")
      )
  )
}

trait ScalateTestSlideShow extends SlideshowRepositoryComponent[Slides] {
  def repository = new TestScalateInMemoryRepository
}
