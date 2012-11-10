name := "intro-to-scalate"

organization := "jackcviers"

version := "0.1.0"

seq(lsSettings :_*)

logLevel in Global := Level.Info

libraryDependencies +=  "ch.qos.logback" % "logback-access" % "1.0.2"

libraryDependencies +=  "ch.qos.logback" % "logback-classic" % "0.9.24"

libraryDependencies += "net.databinder" %% "unfiltered" % "0.6.4"

libraryDependencies += "net.databinder" %% "unfiltered-spec" % "0.6.4" % "test"

libraryDependencies +=   "org.fusesource.scalate" % "scalate-core" % "1.5.3"

libraryDependencies +=  "org.fusesource.scalamd" % "scalamd" % "1.5"

libraryDependencies += "org.fusesource.scalate" % "scalate-util" % "1.5.3" % "test"

libraryDependencies += "org.fusesource.scalate" % "scalate-test" % "1.5.3" % "test"

libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.9.4" % "test"

libraryDependencies += "net.databinder.dispatch" %% "dispatch-jsoup" % "0.9.4" % "test"

libraryDependencies += "org.fluentlenium" % "fluentlenium-core" % "0.7.3" % "test"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.3"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.1"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.1"

resolvers += "unfiltered-resolver-0" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

