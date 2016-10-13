name := "scalagen"
version := "1.0"
scalaVersion := "2.11.8"
organization := "com.scalagen"
crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0-RC1")
exportJars := true
scalacOptions ++= scalaCompileOptions
resolvers ++= Seq(Resolver.jcenterRepo)
coverageEnabled in Test := true
coverageExcludedPackages := "<empty>;com.scalagen.benchmarks.*;com.scalagen.data.Tabulator"
scalafmtConfig := Some(file(".scalafmt.conf"))

lazy val log4j          = "2.7"
lazy val slf4j          = "1.7.21"
lazy val hadoop         = "2.7.3"
lazy val typesafeConfig = "1.3.1"
lazy val parquet        = "1.8.1"
lazy val scalaTest      = "3.0.0"
lazy val breeze         = "0.12"
lazy val shapeless      = "2.3.2"
lazy val ascii          = "0.2.5"

lazy val excludeSlf4j = ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")

lazy val testingDeps = Seq(
  "org.scalatest" %% "scalatest" % scalaTest % Test
)

libraryDependencies ++= Seq(
  "org.apache.logging.log4j" % "log4j-api"        % log4j,
  "org.apache.logging.log4j" % "log4j-core"       % log4j,
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4j,
  "org.apache.logging.log4j" % "log4j-jul"        % log4j,
  "org.slf4j"                % "slf4j-api"        % slf4j,
  "com.typesafe"             % "config"           % typesafeConfig,
  "org.scalanlp"             %% "breeze"          % breeze,
  "org.apache.parquet"       % "parquet-hadoop"   % parquet,
  "org.apache.hadoop"        % "hadoop-common"    % hadoop,
  "com.chuusai"              %% "shapeless"       % shapeless,
  "de.vandermeer"            % "asciitable"       % ascii
).map(_.excludeAll(excludeSlf4j)) ++ testingDeps

lazy val scalaCompileOptions = Seq(
  "-Xlint",
  "-Xfuture",
  "-Xverify",
  "-Xmax-classfile-name",
  "255",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-encoding",
  "UTF-8",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused-import",
  "-language:postfixOps",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions"
)
