import spray.revolver.AppProcess

name := """reactive-turtle"""

version := "1.0"

scalaVersion := "2.11.1"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "com.typesafe.akka"   %%  "akka-actor"    % "2.3.4",
  "com.typesafe.akka"   %%  "akka-remote"   % "2.3.4"
)

Revolver.settings

mainClass in Revolver.reStart := Some("com.github.ktonga.reactiveturtle.internal.ReactiveTurtle")

initialCommands in console :=
  """
    |import com.github.ktonga.reactiveturtle._
    |import Commands._
    |import TurtleRC._
  """.stripMargin

cleanupCommands in console :=
  """
    |shutdown()
  """.stripMargin

addCommandAlias("tg-start", "re-start")

addCommandAlias("tg-stop", "re-stop")
