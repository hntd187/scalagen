import BuildSettings._
import Deps._

lazy val commonSettings = Seq(
  version := "0.0.2",
  scalaVersion := "2.12.2",
  organization := "com.scalagen",
  exportJars := true,
  scalacOptions ++= scalaCompileOptions,
  fork in Test := true,
  resolvers ++= Seq(Resolver.jcenterRepo),
  coverageEnabled in Test := true,
  coverageExcludedPackages := "<empty>;com.scalagen.benchmarks.*;com.scalagen.data.Tabulator;com.scalagen.macros.*;",
  libraryDependencies ++= coreDeps,
  addCompilerPlugin("org.scalameta" % "paradise" % paradise cross CrossVersion.full)
)

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(releaseSettings)
  .aggregate(core, api, macros)

lazy val core = project
  .in(file("core"))
  .settings(commonSettings)
  .dependsOn(macros, api)

lazy val api = project
  .in(file("api"))
  .settings(commonSettings)
  .dependsOn(macros)

lazy val macros = project
  .in(file("macros"))
  .settings(commonSettings)