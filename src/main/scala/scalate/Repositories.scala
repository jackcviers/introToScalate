package scalate

trait SlideshowRepository[+A] {
  def read: A
}

trait SlideshowRepositoryComponent[+A] {
  def repository: SlideshowRepository[A]
}

class ScalateInMemoryRespository extends SlideshowRepository[Slides] {
  def read = Slides(
    Seq(
      Title(1, "Scalate", "The write less do more Scala templating engine.", "Jack Viers", "jackcviers@gmail.com", "jackviers", ""),
      Headline(2, "Besides black art, there is only automation and mechanization.", "-- Frederico Garcia Lorca", "", ""),
      Bullet(3, "Scalate is both", Seq(
	Point("Write less Scala."),
	Point("Easily separate concerns."),
	Point("Automate your view Logic."),
	Point("Know the magic incantations."),
	Point("Only from the (correct) source comes the true word.")
      ), "Setting scalate up can be a pain if you don't know about its rough edges. But it can also be a boon to productivity once you do."),
      Bullet(4, "Using Scalate with Unfiltered", Seq(
	Point("While the scalate console allows for an easy Scalate setup, more than likely you are probably not going to be using WebSphere or Tomcat or another .war server. If you are, the Scalate docs and/or user group are great resources."),
	Point("Second, Unfiltered is a very lightweight webserver that is easy to use."),
	Point("Third, Unfiltered packages for deployment with sbt-assembly very easilly."),
	Point("Fourth, unfiltered-scalate is a good example of how to set up Scalate to work with your framework or toolkit of choice."),
	Point("Fith, unfiltered-scalate illustrates well the places where Scalate can hurt you, thus allowing you to avoid frustration by having attending this talk, and deploying a fast and lean server.")), ""),
      References(5, "Scalate resources", Seq(
	ReferenceLink("Scalate Setup(For .war deployments)", "http://scalate.fusesource.org/documentation/installing.html"),
	ReferenceLink("Scalate User Guide", "http://scalate.fusesource.org/documentation/user-guide.html"),
	ReferenceLink("Scalate Embedding Guide", "http://scalate.fusesource.org/documentation/scalate-embedding-guide.html"),
	ReferenceLink("General Scalate Documentation", "http://scalate.fusesource.org/documentation/index.html"),
	ReferenceLink("Scalate ScalaDocs", "http://scalate.fusesource.org/documentation/index.html"),
	ReferenceLink("This presentation", "https://github.com/jackcviers/introToScalate")
      ), ""),
      CodeExample(6, "Getting unfiltered-scalate", 
		  "\n $ git clone git://github.com/unfiltered/unfiltered-scalate.git "+
		  "\n $ sbt "+
		  "\n > + publish-local", 
		  "Unfiltered scalate doesn't have a published build availabe for sbt resolvers. You have to pull down the source and deploy it locally. It can cross-publish to 2.9.1 and 2.9.2."),
      CodeExample(7, "Set up your project directory Structure", 
		  "\n $ mkdir -p myProject/src/main/scala \\" +
		  "\n myProject/src/main/resources/templates \\" +
		  "\n myProject/src/main/resources/scalate/layouts \\" +
		  "\n myProject/src/main/resources/public \\" +
		  "\n myProject/project myProject/src/test \\", 
		  "The directory structure should be familiar, as it is a typical sbt project structure. The main differences are the resources/templates and resources/scalate/layouts directory, which are particular to Scalate. They hold the layouts, the base templates, and any view templates for the scalate package."),
      CodeExample(8, "project/plugins.sbt", """
resolvers += Resolver.url(
  "artifactory",
  url(
    "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"
  ))(Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n" %% "sbt-assembly" % "0.8.5")""", "For fat jar deployment."
		),
      CodeExample(9, "build.sbt", """
import sbtassembly.Plugin._

import AssemblyKeys._

assemblySettings
...
libraryDependencies ++= Seq(
  "net.databinder" %% "unfiltered" % "0.6.4",
  "net.databinder" %% "unfiltered-scalate" % "0.6.3",
  "net.databinder" %% "unfiltered-jetty" % "0.6.4",
  "net.databinder" %% "unfiltered-filter-async" & "0.6.4",
  "org.fusesource.scalate" % "scalate-core" % "1.5.3",
  "org.fusesource.scalate" % "scalamd" % "1.5",
  "org.fusesource.scalate" % "scalate-util" % "1.5.3",
  "org.fusesource.scalate" % "scalate-test"  % "1.5.3" % "test",
  "net.databinder.dispatch" %% "dispatch-core" % "0.9.5" % "test",
  "net.databinder.dispatch" %% "dispatch-jsoup" % "0.9.4" % "test",
  "org.fluentlenium" % "fluentlenium-core" % "0.7.3" % "test"
)

resolvers += "unfiltered-resolver-0" at "https://oss.sonatype.org/" + 
    "content/repositories/releases"""", 
	"The unfiltered libraries are for the jetty server setup, you need the scala md only for jade filters, and the dispatch and fluentlenium libraries are for testing."
      ),
      Bullet(10, "Steps for running Scalate", Seq(
	Point("1. Configure your template engine."),
	Point("2. Set your template base directory."),
	Point("3. (Optional) Create a layout."),
	Point("4. Create your templates."),
	Point("5. (Optional) Create a domain model and view model."),
	Point("6. (Optional) Create your model views."),
	Point("7. (Optional) Return the result of calling your renderer as a response.")
      ), ""),
      CodeExample(11, "Plans.scala", 
	"""
package scalate
import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.layout.DefaultLayoutStrategy

trait PlanShared {
  implicit val engine = new TemplateEngine
  engine.layoutStrategy = new DefaultLayoutStrategy(
	  engine, "scalate/layouts/default.jade")
  val baseDirectory = "templates/"
}
	""",
	""
      ),
      CodeExample(12, "src/main/resources/layouts/\ndefault.jade",
	"""
-@ val body: String
-@ val title: String = "Some Default Title"

!!! 5
html
  head
    title #{title}
    ...
  body
...
    - unescape(body)
...
	""", 
	"This is Jade syntax, but there are others, including a jsp variant called, imaginatively, ssp, Mustache, and a haml variant called Scaml. If we have time I'll go over an entrely differnt method of templating called Scuery."
      ),
      CodeExample(13, "src/main/resources/templates/\nindex.jade", 
	"""
-@ val slides: scalate.Slides
- layout("../scalate/layouts/default.jade")
  - view(slides)	
	""",
	""
      ),
      CodeExample( 14,
	"Models.scala",
	"""
sealed trait Slide{
  def title: String
  def index: Int
}
case class Title(index: Int, title: String, subtitle: String, 
    author: String, email: String, twitter: String, 
notes: String) extends Slide
case class Bullet(index: Int, title: String, 
    list: Seq[Point], notes: String ) extends Slide
...
case class Point(text: String)
...
sealed trait SlidesTrait[+A] {
  def slides: Seq[A]
}
case class Slides(slides: Seq[Slide]) extends SlidesTrait[Slide]
	""", 
	""
      ),
      CodeExample( 
	15,
	"Bullet.index.jade",
	"""
.slide(id="slide#{index}")
  header #{title}
  section
    ul
      - collection(list)
  aside.note
    section 
      p 
        | #{notes}
	""",
	""
      ),
      CodeExample(
	16,
	"Point.index.jade",
	"""
li
  | #{text}
	""",
	""
      ),
      CodeExample(
	17,
	"Plans.scala",
	"""
import unfiltered.filter.async.Plan
import unfiltered.request.{GET, Path}
import unfiltered.response.{HtmlContent, Ok}
import unfiltered.scalate.Scalate
...
object AsyncPlan extends Plan with PlanShared with ScalateSlideShow {
  def intent = {
    case req@GET(Path("/")) ⇒
      val title = "Scalate: The write less do more scala" + 
      " templating engine."
      req.respond(Ok ~> HtmlContent ~> 
		  Scalate(req, baseDirectory + "index.jade", 
			  ("slides" → repository.read))(
			    additionalAttributes = ("title" → title) :: 
		  Nil))
      
  }
	""",
      ""
      ),
      Bullet(18, "Scalate's Secret Sauce", 
	Seq(
	  Point("There are a few special templating methods in Scalate."),
	  Point("view(viewModel, viewName) = shows the ModelClass.viewName.templateType template using the viewModel value passed to the template engine."),
	  Point("collection(item, viewName) = shows all items in a traversable with the ItemClass.viewName.templateType template"),
	  Point("layout(path/to/layoutTemplate) = wraps everything in its block in the specified layout."),
	  Point("render(template, attributesMap) = renders the template as a string at the place the render was called, optionally passing a Map of attributes to a template"),
	  Point("capture{ ? => String } = captures the string output (usually from a template render call) to re-use in the current template block.")
	),	  
	"There are three special Scalate template functions - view, collection, and layout, that show a single view, a series of views, and wrap templates in repetitive markup, respectively."
      ),
      Headline(19, "These functions are independent of syntax type.", "They are a templating DSL.", "The real power of Scalate is the ability to re-use different blocks with these calls, and the ability to auto-render views associated with different models.", ""),
      Bullet(20, "Template syntaxes", Seq(
	Point("Jade"),
	Point("Scaml"),
	Point("SSP"),
	Point("Mustache")
      ), ""),
      CodeExample(21, "Jade", """
.slide(id="slide#{index}")
  section.middle
    p #{title}
    p #{subtitle}
    p 
      | #{author}
      i Hacker
    p
      :&markdown
        [#{email}](mailto:#{email})
    p
      :&markdown
        [@#{twitter}](http://twitter.com/#{twitter})
  aside.note
    section 
      p #{notes}  
      """, "Blocks are indicated by indentation level. Divs are implied. #{} = scala interpolation. | indicates plain text. Filters specified by :. Filters supporting scala interpolation are specified by :&. \".\" indicates class name. \"#\" indicates id. strings beginning a line of non-plaintext or filtered content are an element name. Elements don't have to be manually closed. A space and content after an element makes the element block single line. Nested plaintext blocks can be used to contain more than one content piece."),
      CodeExample(22, 
		      "Scaml", 
		      """
%p
  -@ val listString = List("hi", "there", "reader!").mkString(" ")
  #{listString}
  = "yo"
#myDiv.scala="Dawg!"
		      """, 
		      "Similar to Jade, but without markdown syntax. Plain text doesn't have to be delimited."
      ),
      CodeExample(23, 
		      "SSP", 
		      """
<%@ val bar: String = "beamer" %>
<%@ val ham: Boolean = true %>
<p>
<%
  var foo = "hello"
  foo += " there"
  foo += " you!"
%>
${foo}
${bar}
</p>
<ul>
#if(ham)
  <li>Eggs</li>
#for (i <- 1 to 5)
  <li>${i}</li>
#end

		      """, 
		      "Not dry. Similar to JSP. Velocity Style directives. Scala bindings use <%@. Anything in <% %> is evaluated as Scala code. Whitespace indefferent. ${} interpolation."
      ),
      Headline(
		      24, 
		      "Mustache", 
		      "DO NOT USE unless you need to evaluate lots of stuff at runtime.",
		      "You can see the reference on the syntax pages of the Scalate documentation. It is slower than the other templating methods, doesn't conform to the layout and views dsl, and isn't as dry as Scaml or Jade. Uses plain HTML with {{}} mustache tags.",
		      ""
      ),
      Headline(25, " ", "Testable templates.", "", ""),
      Bullet(26, "Using Selenium", Seq(
		      Point("Set up a test plan."),
		      Point("Use xpath to select elements."),
		      Point("Set the server to the server using your test plan."),
		      Point("Use assert and xpath to run your tests."),
		      Point("Be sure to shut your sever down after running tests.")
      ), ""),
      Headline(27, " ", "A little example in the terminal.", "", ""),
      Bullet(28, "Scuery", Seq(
		      Point("Transforms well-formatted html and replaces content with the value using a transformer."),
		      Point("Means if you are working with a designer, you can just let them go hog-wild with html, and put the dynamic values in later using Scuery!"),
		      Point("Uses css3 selectors and a jQuery like function to transform content. $FTW"),
		      Point("Might be slow for production use, haven't tested."),
		      Point("Can use the $ method to query for node existence as well, so can be useful in tests.")
      ), ""),
      CodeExample(29, "Using Scuery", 
		      """
import org.fusesource.scalate.scuery._
import org.fusesource.scalate.scuery.Transform._
...		      
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
  def intent {
  ...
    case req@GET(Path("/scuery")) ⇒
      req.respond(Ok ~> HtmlContent ~> ResponseString(result.toString) )
  }
		      """, ""),
      Title(30, "Fin", "Scalate: The write less do more Scala templating engine.", "Jack Viers", "jackcviers@gmail.com", "jackviers", "")
    )
  )
}

trait ScalateSlideShow extends SlideshowRepositoryComponent[Slides] {
  def repository = new ScalateInMemoryRespository
}
