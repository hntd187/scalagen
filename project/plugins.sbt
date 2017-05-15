logLevel := Level.Warn
resolvers += Classpaths.sbtPluginReleases
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC1")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.6.8")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.4")
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.24")
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.4.0")
