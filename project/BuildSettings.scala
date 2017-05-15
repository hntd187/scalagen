import bintray.BintrayPlugin.autoImport._
import sbt.Keys._
import sbt._

object BuildSettings {

  lazy val releaseSettings = Seq(
    crossScalaVersions := Seq("2.11.11", "2.12.2"),
    description := "A Scala library for generating row based test data",
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    bintrayPackageLabels := Seq("testing", "scala", "jvm", "data"),
    bintrayVcsUrl := Some("git@github.com:hntd187/scalagen.git")
  )

  lazy val scalaCompileOptions = Seq(
    "-Xlint",
    "-Xfuture",
    "-Xverify",
    "-Xmax-classfile-name",
    "255",
    "-Xplugin-require:macroparadise",
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

}
