package scalate

import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import unfiltered.filter.async.Plan
import unfiltered.request.{GET, Path}
import unfiltered.response.{HtmlContent, Ok, ResponseString}
import unfiltered.scalate.Scalate
import org.fusesource.scalate.scuery._
import org.fusesource.scalate.scuery.Transform._



trait PlanShared {
  implicit val engine = new TemplateEngine
  engine.layoutStrategy = new DefaultLayoutStrategy(
	  engine, "scalate/layouts/default.jade")
  val baseDirectory = "templates/"
}

object AsyncPlan extends Plan with PlanShared with ScalateSlideShow {
  object transformer extends Transformer {
    $(".person") { node ⇒
       people.flatMap { p ⇒
         new Transform(node) {
	   $(".name").contents = p.name
	   $(".location").contents = p.location
         }.toNodes
       }
    }
  }
  lazy val html = 
    <html lang="en">
      <head>
	<title> Scuery </title>
      </head>
      <body>
	<div id="content">
	  <table class="people">
	    <tr>
	      <th>Name</th>
	      <th>Location</th>
	    </tr>
	    <tr class="person">
	      <td class="name">DummyName</td>
	      <td class="location">DummyLocation</td>
	    </tr>
	  </table>
	</div>
      </body>
    </html>
  
  lazy val people = List(Person("James", "Des Moines"), Person("Jack", "Key West"))
  lazy val result = transformer(html)

  def intent = {
    case req@GET(Path("/")) ⇒
      val title = "Scalate: The write less do more scala templating engine."
      req.respond(Ok ~> HtmlContent ~> Scalate(req, baseDirectory + "index.jade", ("slides" → repository.read))(additionalAttributes = ("title" → title) :: Nil))
    case req@GET(Path("/scuery")) ⇒
      req.respond(Ok ~> HtmlContent ~> ResponseString(result.toString) )
      
  }
}
