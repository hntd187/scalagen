import sbt._

object Deps {

  lazy val log4j          = "2.8.2"
  lazy val slf4j          = "1.7.25"
  lazy val hadoop         = "2.8.0"
  lazy val typesafeConfig = "1.3.1"
  lazy val parquet        = "1.9.0"
  lazy val scalaTest      = "3.0.3"
  lazy val breeze         = "0.13.1"
  lazy val shapeless      = "2.3.2"
  lazy val ascii          = "0.3.2"
  lazy val meta           = "1.7.0"
  lazy val paradise       = "3.0.0-M8"

  lazy val excludeSlf4j = ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")

  lazy val testingDeps: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % scalaTest % Test
  )

  lazy val macroDeps: Seq[ModuleID] = Seq(
    "org.scalameta" %% "scalameta" % meta % Provided
  )

  lazy val loggingDeps: Seq[ModuleID] = Seq(
    "org.apache.logging.log4j" % "log4j-api"        % log4j,
    "org.apache.logging.log4j" % "log4j-core"       % log4j,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4j,
    "org.slf4j"                % "slf4j-api"        % slf4j
  )

  lazy val coreDeps: Seq[ModuleID] = (Seq(
    "com.typesafe"       % "config"         % typesafeConfig,
    "org.scalanlp"       %% "breeze"        % breeze,
    "org.apache.parquet" % "parquet-hadoop" % parquet,
    "org.apache.hadoop"  % "hadoop-common"  % hadoop,
    "de.vandermeer"      % "asciitable"     % ascii
  ) ++ loggingDeps ++ testingDeps ++ macroDeps).map(_.excludeAll(excludeSlf4j))

}
