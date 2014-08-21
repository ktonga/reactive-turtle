package com.github.ktonga.reactiveturtle

object TurtleShapesExample extends App with TurtleRC with Shapes {

  def commands: Seq[Command] =
    layout(
      Seq((200, 200), (400, 200), (300, 400)),
      Seq(square(50), circle(5), spiral(20))
    )

  runAllAndWait(commands)
  system.shutdown()

}

trait Shapes {

  type Shape = Seq[Command]

  def square(size: Int): Shape = {
    (1 to 4).flatMap(_ => Seq(Forward(size), Right(90)))
  }

  def spiral(size: Int): Shape = {
    (1 to 100).flatMap(a => Seq(Forward(size), Right(110 - a)))
  }

  def circle(size: Int): Shape = {
    (1 to 90).flatMap(a => Seq(Forward(size), Right(4)))
  }

  def layout(points: Seq[(Int, Int)], shapes: Seq[Shape]): Seq[Command] =
    points.zip(shapes).flatMap {case ((x, y), cmds) => PenUp +: SetXY(x, y) +: PenDown +: cmds}

}

