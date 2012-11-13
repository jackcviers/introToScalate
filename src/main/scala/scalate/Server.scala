package scalate

import unfiltered.jetty.Http

object Served extends Application {

  override def main(args: Array[String]) {
    Http.local(8080).filter(AsyncPlan).run()
  }

}

