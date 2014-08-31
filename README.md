reactive-turtle
===============

Teach Typesafe Stack (Scala and Akka) to the kids the funny way

  - Launch the Turtle Graphics in a separate window
  - Command the Turtle from a separate JVM with the Remote Control
  - Defer side-effects to the very end of the program
  - Define your program in a Functional way composing simple Functions
  - Define your program based on Akka Actors
  - Use the IDE of your choise. Vim + SBT, REPL, Idea, Eclipse, etc.

The Story
---------

This, is a story, about a father and his son, then about the father the father the father, and then again his son.

#### The father and his son part

I have a son called Elian, he’s 11 and he’s very smart, so I decided to teach him programming.
Obviously I love him, so I choose to go with **Scala**. 
I started with some basics, vals and defs for doing some maths, but it quickly became boring so my son lost the interest.

The first thing that came to my mind was LOGO, playing with the turtle is great for learning programming. Since I use KDE (that’s why the K stand for in my nickname ktonga) I’ve installed KTurtle, it’s a nice educational app, but sadly as every LOGO’s clone out there, it’s based on an imperative language. And I wanted to teach my son FP.


#### So this is the father the father the father part of the story

I started researching turtle programming environments, I found many for Java and a very nice one for Scala, but with a pretty imperative approach, so I didn’t like it.

I decided to create my own, but I didn’t want to focus in the IDE part as all the rest does. So much effort on syntax highlight, auto-complete, errors marks, etc.. Nothing of that is needed since lot of environments already exist that do it fantastically.
I was lucky, and one of the Java based environments has a standalone graphics part which I use to command remotely from a program developed in the IDE of my choice.

The idea of reactive turtle is to command the turtle in a functional way, so I designed a minimal API to separate the program’s instructions from the side effects (moving the turtle). You can compose pure functions to create a list of commands which then will be run by the turtle

#### The son part again

Once I finished, I resumed the teaching to my son again.

Try it out and let me know what you think!


Get Started
-----------

```sh
git clone https://github.com/ktonga/reactive-turtle.git
cd reactive-turtle
sbt compile
```

Playing with the Turtle
-----------------------

### Start the Turtle Graphics (TG) Window

The first you need to do in order to use `Reactive Turtle` is to start the TG, it will open a window with the canvas and will start listening for remote `Commands`. You do it by launching the SBT shell and executing the task `tg-start`. It will run in a background JVM and will return back the SBT shell prompt. Optionaly you can pass width and height args. They are 600 600 by default.

```sh
sbt
[info] Set current project to reactive-turtle
> tg-start 500 500
```

You can terminate it, closing the window or executing the task `tg-stop` in the SBT shell.

```sh
> tg-stop
```

![tg-start](https://raw.githubusercontent.com/ktonga/reactive-turtle/master/tutorial/tg-start.png)


### Scripting with the Turtle

We have the turtle, now we want to tell it some movements. The fastest way to start commanding the turtle is by launching the Scala REPL. You can do it right in the SBT session you have open for the TG window. Execute the console task, it will start with all the needed imports already evaluated.
You have many ways to send Commands to the turtle, the easiest ones are the synchronous. When you'r done with the console, type `:q` for going back to SBT shell prompt.

```scala
// run var args Commands
runAndWait(SetXY(100, 400), Clean)

// run a Seq of Commands
val commands = Seq.fill(4)(Seq(Forward(300), Right(90))).flatten
runAllAndWait(commands)

// run a single Command
PenUp.rnw
Home.rnw
```

You can take a look at [`TurtleRC`](https://github.com/ktonga/reactive-turtle/blob/master/src/main/scala/com/github/ktonga/reactiveturtle/rc.scala) trait for available methods and [`Commands`](https://github.com/ktonga/reactive-turtle/blob/master/src/main/scala/com/github/ktonga/reactiveturtle/package.scala) object for available commands.

![tg-start](https://raw.githubusercontent.com/ktonga/reactive-turtle/master/tutorial/console.png)


### Functional Programming in Scala with the Turtle

I'm using the [Turtle Graphics](http://www.bfoit.org/itp/JavaTurtleGraphics.html) implemented by [BFOIT](http://www.bfoit.org/) which is done in Java, obviously in imperative programming, so all the turtle methods have side-effects. But thanks to a piece of advice from the excelent book [Functional Programming in Scala](http://manning.com/FunctionalProgramminginScala) writen by @pchiusano and @runarorama I've separated the program's logic from the movements that actually has to do the turtle at the end of the program.

You will be able to define the `Seq[Command]` composing all the pure functions you want. For writting your Turtle Scala Program you have to extend the [ScalaApp](src/main/scala/com/github/ktonga/reactiveturtle/rc.scala) trait and implement the `def commands: Seq[Command]` method.

```scala
package com.github.ktonga.reactiveturtle.example

import com.github.ktonga.reactiveturtle._
import Commands._

object ScalaAppExample extends ScalaApp with Shapes {

  def commands: Seq[Command] =
    layout(
      Seq((200, 200), (400, 200), (300, 400)),
      Seq(square(50), circle(5), spiral(20))
    )

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

```

You can run your programs, or any of the included [examples](src/main/scala/com/github/ktonga/reactiveturtle/example), executing the `run` task in the SBT session and chosing one from the list.

![tg-start](https://raw.githubusercontent.com/ktonga/reactive-turtle/master/tutorial/run-scala.png)

### hAkking with the Turtle

Once your child becomes a Scala expert, you can introduce him/her into the wonderful world of [Akka](http://akka.io/). The TG is represented by an `Actor` and you'll have to implement your own `Actor` to interact with the TG, sending Commands and receiving [`States`](src/main/scala/com/github/ktonga/reactiveturtle/package.scala). The way to do it, is extending the [`AkkaApp`](src/main/scala/com/github/ktonga/reactiveturtle/rc.scala) trait, defining the `Actor` and implementing the `def props: Props` method which must return a `Props` of your `Actor`. The first message the `Actor` will receive will be a `State` case class and the sender will be the TG `Actor`. The program ends when your `Actor` terminates.

```scala
package com.github.ktonga.reactiveturtle.example

import akka.actor.{PoisonPill, Actor, Props}
import com.github.ktonga.reactiveturtle._
import internal.TurtleGraphicsActor.Execute
import Commands._


object AkkaAppExample extends AkkaApp {
  override def props: Props = Props[CommandsActor]
}

class CommandsActor extends Actor {

  val step = 5
  val inclination = 5

  val heading2right = 90 + inclination
  val heading2left = 270 - inclination

  def receive: Receive = {
    case state @ State(Canvas(w, _), _) =>
      sender ! Execute(Seq(Clean, PenUp, SetXY(0, 0), PenDown, Heading(heading2right)))
      context.become(left2right(w))
  }

  val right2left = moving(x => x - step <= 0) {
    sender ! Heading(heading2right)
    context.unbecome()
  }

  def left2right(w: Int) = moving(x => x + step >= w) {
    sender ! Heading(heading2left)
    context.become(right2left, discardOld = false)
  }

  def moving(stop: Int => Boolean)(next: => Unit): Receive = {
    case State(Canvas(_, h), Turtle(_, y, _, _, _)) if y + 10 >= h =>
      self ! PoisonPill
    case State(_, Turtle(x, _, _, _, _)) if stop(x) =>
      next
    case s =>
      sender ! Forward(step)
  }
}

```

The way for running it is the same as for the Scala example.

![run-akka](https://raw.githubusercontent.com/ktonga/reactive-turtle/master/tutorial/run-akka.png)

### Ready!

Now, enjoy of quality time with your children.
