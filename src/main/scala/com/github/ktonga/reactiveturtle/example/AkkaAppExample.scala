package com.github.ktonga.reactiveturtle.example

import akka.actor.{PoisonPill, Actor, Props}
import com.github.ktonga.reactiveturtle._
import internal.TurtleGraphicsActor.Execute
import Commands._


object AkkaAppExample extends AkkaApp {
  override def props: Props = Props[CommandsActor]
}

class CommandsActor extends Actor {

  val step = 5
  val inclination = 5

  val heading2right = 90 + inclination
  val heading2left = 270 - inclination

  def receive: Receive = {
    case state @ State(Canvas(w, _), _) =>
      sender ! Execute(Seq(Clean, PenUp, SetXY(0, 0), PenDown, Heading(heading2right)))
      context.become(left2right(w))
  }

  val right2left = moving(x => x - step <= 0) {
    sender ! Heading(heading2right)
    context.unbecome()
  }

  def left2right(w: Int) = moving(x => x + step >= w) {
    sender ! Heading(heading2left)
    context.become(right2left, discardOld = false)
  }

  def moving(stop: Int => Boolean)(next: => Unit): Receive = {
    case State(Canvas(_, h), Turtle(_, y, _, _, _)) if y + 10 >= h =>
      self ! PoisonPill
    case State(_, Turtle(x, _, _, _, _)) if stop(x) =>
      next
    case s =>
      sender ! Forward(step)
  }
}
