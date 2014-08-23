package com.github.ktonga.reactiveturtle.internal

import akka.actor.{Actor, Props}
import com.github.ktonga.reactiveturtle.Command

object TurtleGraphicsActor {

  // Protocol
  case class Execute(command: Command)
  case class ExecuteAll(commands: Seq[Command])

  // Props
  def props: Props = Props[TurtleGraphicsActor]
}

class TurtleGraphicsActor extends Actor {
  import com.github.ktonga.reactiveturtle.internal.TurtleGraphicsActor._

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
