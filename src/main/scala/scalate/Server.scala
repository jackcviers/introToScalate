import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.fusesource.scalate._
import org.fusesource.scalate.util.FileResourceLoader
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import java.io.File

trait Server {
  val log: Logger = LoggerFactory.getLogger(this.getClass)
  val sourceDirectories = new File(getClass.getResource("templates").getFile()) :: Nil
  val engine = TemplateEngine(sourceDirectories, "production")
  engine.layoutStrategy = new DefaultLayoutStrategy(engine, TemplateEngine.templateTypes.map("scalate/layouts/default." + _):_*)
}

object Served extends Application with Server {

  override def main(args: Array[String]) {
    val output = engine.layout("index.jade")
    log.info(output)
  }

}
