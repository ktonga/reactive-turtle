name := """reactive-turtle"""

version := "1.0"

scalaVersion := "2.11.1"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "com.typesafe.akka"   %%  "akka-actor"    % "2.3.4",
  "com.typesafe.akka"   %%  "akka-remote"   % "2.3.4"
)

