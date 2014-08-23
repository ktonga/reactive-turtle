package com.github.ktonga.reactiveturtle.internal

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object ReactiveTurtle extends App {

  implicit val system = ActorSystem("reactive-turtle", ConfigFactory.load("reactive-turtle"))

  val (width, height) = args.toList match {
    case w :: h :: Nil => (w.toInt, h.toInt)
    case _ => (600, 600)
  }

  system.actorOf(TurtleGraphicsActor.props(width, height), "tg")

}
