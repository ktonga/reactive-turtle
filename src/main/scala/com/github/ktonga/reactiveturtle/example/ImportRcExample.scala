package com.github.ktonga.reactiveturtle.example

import com.github.ktonga.reactiveturtle._
import Commands._
import TurtleRC._

object ImportRcExample extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  def withPenUp(cmds: Command*) = PenUp +: cmds :+ PenDown

  Clean.rnw

  await(
    for {
      State(w, h, _, _, _, _, _) <- runAll( withPenUp( SetXY(0, 0), PenSize(8) ))
      _ <- run( SetXY(w, h) )
      _ <- runAll( withPenUp( SetY(0) ))
      state <- run( SetXY(0, h) )
    } yield state
  )

  runAllAndWait( withPenUp( Home, Forward(50) ))
}
