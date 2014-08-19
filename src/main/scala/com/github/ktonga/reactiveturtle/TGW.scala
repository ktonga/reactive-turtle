package com.github.ktonga.reactiveturtle

import org.bfoit.tg.TurtleGraphicsWindow

class TGW(width: Int, height: Int) extends TurtleGraphicsWindow(width, height) {
  import Commands._

  def execute(command: Command) = command match {
    case Clean => clean()
    case PenDown => pendown()
    case PenUp => penup()
    case Forward(steps) => forward(steps)
    case Right(degrees) => right(degrees)
    case SetXY(x, y) => setxy(x2tg(x), y2tg(y))
  }

  private def x2tg(x: Int) = x - (width / 2)
  private def y2tg(y: Int) = -(y - (height / 2))

}

object TGW {
  def apply(width: Int, height: Int) = new TGW(width, height)
}
