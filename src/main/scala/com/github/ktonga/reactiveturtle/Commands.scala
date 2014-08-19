package com.github.ktonga.reactiveturtle

object Commands {

  sealed trait Command

  case object Clean extends Command
  case object PenUp extends Command
  case object PenDown extends Command
  case class Forward(steps: Int) extends Command
  case class Right(degrees: Int) extends Command
  case class SetXY(x: Int, y: Int) extends Command

}
