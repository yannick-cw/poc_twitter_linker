
name := "twitter_crawler"
version := "0.1-SNAPSHOT"
scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("releases")


libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % Test,
  "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.4.7",
  "com.typesafe.akka" % "akka-stream_2.11" % "2.4.7",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.7",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.7",
  "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.7",
  "com.danielasfregola" %% "twitter4s" % "0.2"
)