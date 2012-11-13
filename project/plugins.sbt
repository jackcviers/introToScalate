resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

resolvers ++= Seq(
  "less is" at "http://repo.lessis.me",
  "coda" at "http://repo.codahale.com")

addSbtPlugin("com.eed3si9n" %% "sbt-assembly" % "0.8.5")

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.2")
