
name := """reactive-turtle"""

version := "1.0"

scalaVersion := "2.11.1"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "com.typesafe.akka"   %%  "akka-actor"    % "2.3.4",
  "com.typesafe.akka"   %%  "akka-remote"   % "2.3.4"
)

Revolver.settings

mainClass in Revolver.reStart := Some("com.github.ktonga.reactiveturtle.ReactiveTurtle")

initialCommands in console :=
  """
    |import com.github.ktonga.reactiveturtle._
    |import TurtleRC._
  """.stripMargin
