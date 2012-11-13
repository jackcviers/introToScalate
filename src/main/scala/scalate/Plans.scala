package scalate

import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import unfiltered.filter.async.Plan
import unfiltered.request.{GET, Path}
import unfiltered.response.{HtmlContent, Ok}
import unfiltered.scalate.Scalate


trait PlanShared {
  implicit val engine = new TemplateEngine
  engine.layoutStrategy = new DefaultLayoutStrategy(
	  engine, TemplateEngine.templateTypes.map("scalate/layouts/default." + _):_*)
  val baseDirectory = "templates/"
}

object AsyncPlan extends Plan with PlanShared {
  def intent = {
    case req@GET(Path("/")) ⇒
      val title = "Scalate: The write less do more scala templating engine."
      req.respond(Ok ~> HtmlContent ~> Scalate(req, baseDirectory + "index.jade")(engine = engine, additionalAttributes = ("title" → title) :: Nil))
      
  }
}
