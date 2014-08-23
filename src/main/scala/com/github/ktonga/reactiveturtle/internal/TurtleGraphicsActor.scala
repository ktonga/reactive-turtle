package com.github.ktonga.reactiveturtle.internal

import akka.actor.{Actor, Props}
import com.github.ktonga.reactiveturtle.Command

object TurtleGraphicsActor {

  // Protocol
  case class Execute(commands: Seq[Command])
  case object GetState

  // Props
  def props(width:Int, height: Int): Props = Props(new TurtleGraphicsActor(width, height))
}

class TurtleGraphicsActor(width: Int, height: Int) extends Actor {
  import com.github.ktonga.reactiveturtle.internal.TurtleGraphicsActor._

  val tgw = TGW(width, height)

  override def receive: Receive = {
    case Execute(cmds) =>
      cmds.foreach(tgw.execute)
      sender ! tgw.tgState
    case GetState => sender ! tgw.tgState
  }

}
