package com.github.ktonga.reactiveturtle

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import com.github.ktonga.reactiveturtle.internal.TurtleGraphicsActor
import com.typesafe.config.ConfigFactory
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import TurtleGraphicsActor._

trait TGRef {
  val remotePath = "akka.tcp://reactive-turtle@127.0.0.1:2552/user/tg"

  def configName = "turtle-rc"

  implicit val system = ActorSystem("turtle-rc", ConfigFactory.load(configName))
  val tgRefFtr = system.actorSelection(remotePath).resolveOne(10.seconds)

  def shutdown() = system.shutdown()

}

trait TurtleRC extends TGRef {

  def runAll(commands: Seq[Command]): Future[State] = {
    implicit val askTimeout: Timeout = 1.minute
    tgRefFtr flatMap { ref => (ref ? Execute(commands)).mapTo[State] }
  }

  def run(commands: Command*): Future[State] = runAll(commands)

  def runAllAndWait(commands: Seq[Command]): State =  await(runAll(commands))

  def runAndWait(commands: Command*): State = runAllAndWait(commands)

  def getState: Future[State] = {
    implicit val askTimeout: Timeout = 1.minute
    tgRefFtr flatMap { ref => (ref ? GetState).mapTo[State] }
  }

  def awaitState: State = await(getState)

  def await[T](ftr: Future[T]): T = Await.result(ftr, Duration.Inf)
}

object TurtleRC extends TurtleRC {

  implicit class CommandOps(val cmd: Command) extends AnyVal {
    def r = run(cmd)
    def rnw = runAndWait(cmd)
  }

}

trait ScalaApp extends App with TurtleRC {

  def commands: Seq[Command]

  runAllAndWait(commands)
  shutdown()
}

trait AkkaApp extends App with TGRef {

  def props: Props

  val commandsRef = system.actorOf(props, "commands")

  val finisher = system.actorOf(Props(new Actor {
    def receive: Receive = {
      case watched: ActorRef => context.watch(watched)
      case Terminated(_) => context.system.shutdown()
    }
  }), "finisher")

  finisher ! commandsRef

  tgRefFtr onSuccess {
    case tgRef => tgRef.tell(GetState, commandsRef)
  }

}