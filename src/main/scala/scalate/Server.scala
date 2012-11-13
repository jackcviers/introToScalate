package scalate

import unfiltered.jetty.Http
import unfiltered.jetty.ContextBuilder


object Served extends Application {

  override def main(args: Array[String]) {
    Http.local(8080).context("/public"){staticContext: ContextBuilder => staticContext.resources(getClass().getResource("/public"))}.filter(AsyncPlan).run()
  }

}

