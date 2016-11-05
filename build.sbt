name := "scalagen"
version := "0.0.2"
scalaVersion := "2.11.8"
organization := "com.scalagen"
crossScalaVersions := Seq("2.10.6", "2.11.8")
description := "A scala library for generating row based test data"
exportJars := true
scalacOptions ++= scalaCompileOptions
fork in Test := true
resolvers ++= Seq(Resolver.jcenterRepo)
coverageEnabled in Test := true
coverageExcludedPackages := "<empty>;com.scalagen.benchmarks.*;com.scalagen.data.Tabulator"
scalafmtConfig := Some(file(".scalafmt.conf"))
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
bintrayPackageLabels := Seq("testing", "scala", "jvm", "data")
bintrayVcsUrl := Some("git@github.com:hntd187/scalagen.git")

lazy val log4j          = "2.7"
lazy val slf4j          = "1.7.21"
lazy val hadoop         = "2.7.3"
lazy val typesafeConfig = "1.3.1"
lazy val parquet        = "1.9.0"
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
  "org.slf4j"                % "slf4j-api"        % slf4j,
  "com.typesafe"             % "config"           % typesafeConfig,
  "org.scalanlp"             %% "breeze"          % breeze,
  "org.apache.parquet"       % "parquet-hadoop"   % parquet,
  "org.apache.hadoop"        % "hadoop-common"    % hadoop,
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
  "-language:postfixOps",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-Xlog-free-terms"
)
