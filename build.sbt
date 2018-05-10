name := """fish-shop-slack-bot"""
organization := "cz.jt"

version := "1.0.5-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.1.0"

// tests
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "cz.jt.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "cz.jt.binders._"
