name := "finch-sample"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.13.1",
  "com.github.finagle"  %% "finch-argonaut"  % "0.13.1",
  "com.github.finagle" %% "finch-circe" % "0.13.1",
  "com.twitter" %% "finagle-mysql" % "6.42.0",
  "com.netaporter" %% "scala-uri" % "0.4.16",
  "io.circe" %% "circe-generic" % "0.7.0",

  // test
  "org.specs2" %% "specs2-core" % "3.8.8" % "test"
)

enablePlugins(JavaAppPackaging)

dockerBaseImage in Docker := "java:8-jdk-alpine"

dockerExposedPorts in Docker := Seq(8080, 8080)
