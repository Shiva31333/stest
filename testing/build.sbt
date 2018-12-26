enablePlugins(JavaAppPackaging)
name := "testing"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.6"
//libraryDependencies += "com.google.code.gson" % "gson" % "2.8.5"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.19"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.5"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.19"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.5"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.0.pr1"
