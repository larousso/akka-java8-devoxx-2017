name := """devoxx-2017"""

version := "1.0"

scalaVersion := "2.12.1"

val akkaVersion = "2.5.0-RC2"

libraryDependencies ++= Seq(
  "io.javaslang" % "javaslang" % "2.0.5",
  "org.apache.commons" % "commons-lang3" % "3.5",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.0.5",
  "de.heikoseeberger" %% "akka-sse" % "2.0.0",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)