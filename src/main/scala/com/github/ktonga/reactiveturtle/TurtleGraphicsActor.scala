package com.github.ktonga.reactiveturtle

import akka.actor.{Props, Actor}
import Commands._

object TurtleGraphicsActor {

  // Protocol
  case class Execute(command: Command)
  case class ExecuteAll(commands: Seq[Command])

  // Props
  def props: Props = Props[TurtleGraphicsActor]
}

class TurtleGraphicsActor extends Actor {
  import TurtleGraphicsActor._

  val tgw = TGW(600, 600)

  override def receive: Receive = {
    case Execute(cmd) =>
      tgw.execute(cmd)
      sender ! "OK"
    case ExecuteAll(cmds) =>
      cmds.foreach(tgw.execute)
      sender ! "OK"
  }

}
