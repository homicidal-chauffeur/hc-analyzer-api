name := """hc-analyzer-api"""
organization := "net.nextlogic"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, DebianPlugin)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-json" % "2.7.2",
  "com.typesafe.play" %% "play-json-joda" % "2.7.2",

  "org.postgresql" % "postgresql" % "42.2.5",
  "com.github.tminglei" %% "slick-pg" % "0.18.0",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.18.0",
  "com.github.tminglei" %% "slick-pg_core" % "0.18.0",
  "com.github.tminglei" %% "slick-pg_jts" % "0.18.0"
)

routesImport += "play.api.mvc.PathBindable.bindableUUID"

maintainer in Linux := "NextLogic Pte Ltd <peter@nextlogic.biz>"
packageSummary in Linux := "API for Homicidal Chauffeur Analyzer"
packageDescription := "API for Homicidal Chauffeur Analyzer"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "net.nextlogic.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "net.nextlogic.binders._"
