package com.github.ktonga

package object reactiveturtle {

  sealed trait Command

  object Commands {
    case object Clean extends Command
    case object Home extends Command
    case object PenUp extends Command
    case object PenDown extends Command
    case class Forward(steps: Int) extends Command
    case class Back(steps: Int) extends Command
    case class Right(degrees: Int) extends Command
    case class Left(degrees: Int) extends Command
    case class SetX(x: Int) extends Command
    case class SetY(y: Int) extends Command
    case class SetXY(x: Int, y: Int) extends Command
    case class Heading(degrees: Int) extends Command
    case class PenSize(penWidth: Int) extends Command
  }

  case class State(width: Int, height: Int,
                   x: Int, y: Int, heading: Double,
                   penDown: Boolean, penSize: Int)

}
