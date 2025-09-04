import sbt.Compile

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .settings(
    name := "PriceBasket",
    idePackagePrefix := Some("com.assignment.pricebasket"),
    Compile / mainClass := Some("com.assignment.pricebasket.PriceBasket")
  )

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.4.0"
)
