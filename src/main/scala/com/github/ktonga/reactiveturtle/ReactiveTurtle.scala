package com.github.ktonga.reactiveturtle

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object ReactiveTurtle extends App {

  implicit val system = ActorSystem("reactive-turtle", ConfigFactory.load("reactive-turtle"))

  system.actorOf(TurtleGraphicsActor.props, "tg")

}
