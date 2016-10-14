logLevel := Level.Warn
resolvers += Classpaths.sbtPluginReleases
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M14")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.4.0")
addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.4.5")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.10")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.11")
addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")
