package com.github.ktonga.reactiveturtle.internal

import com.github.ktonga.reactiveturtle._
import org.bfoit.tg.TurtleGraphicsWindow
import Commands._

class TGW(width: Int, height: Int)
  extends TurtleGraphicsWindow(width, height) {

  def execute(command: Command) = command match {
    case Clean => clean()
    case Home => home()
    case PenDown => pendown()
    case PenUp => penup()
    case Forward(steps) => forward(steps)
    case Back(steps) => back(steps)
    case Right(degrees) => right(degrees)
    case Left(degrees) => left(degrees)
    case SetX(x) => setx(x2tg(x))
    case SetY(y) => sety(y2tg(y))
    case SetXY(x, y) => setxy(x2tg(x), y2tg(y))
    case Heading(degrees) => setheading(degrees)
    case PenSize(penWidth) => setpensize(penWidth)
  }

  private def x2tg(x: Int) = x - (width / 2)
  private def y2tg(y: Int) = -(y - (height / 2))
  private def tg2x(x: Int) = x + (width / 2)
  private def tg2y(y: Int) = (height / 2) - y

  def tgState = State(
    Canvas(width, height),
    Turtle(tg2x(xcor()), tg2y(ycor()), heading().toInt, ispendown(), pensize())
  )

}

object TGW {
  def apply(width: Int, height: Int) = new TGW(width, height)
}
