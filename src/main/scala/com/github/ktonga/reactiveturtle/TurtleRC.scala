package com.github.ktonga.reactiveturtle

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import TurtleGraphicsActor._
import Commands._

trait TGRef {
  val remotePath = "akka.tcp://reactive-turtle@127.0.0.1:2552/user/tg"

  implicit val system = ActorSystem("turtle-rc", ConfigFactory.load("turtle-rc"))
  implicit val timeout = 10.seconds
  val tgRefFtr = system.actorSelection(remotePath).resolveOne(timeout)
}

trait TurtleRC extends TGRef {

  def run(commands: Seq[Command]): Future[String] = {
    implicit val askTimeout: Timeout = 5.seconds
    tgRefFtr flatMap { ref => (ref ? ExecuteAll(commands)).mapTo[String] }
  }

  def syncRun(commands: Seq[Command]): Unit =  {
    val resp = run(commands)
    Await.ready(resp, 25.seconds)
  }

}

object TurtleRC extends TurtleRC

