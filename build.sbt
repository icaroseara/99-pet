name := """99-pet"""
organization := "com.icaroseara"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  guice,
  ws,
  "org.scalatest" %% "scalatest" % "3.0.4" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.7-play26",
  "com.github.etaty" %% "rediscala" % "1.8.0",
  "org.mockito" % "mockito-core" % "2.11.0" % Test
)

import play.sbt.routes.RoutesKeys
RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"

