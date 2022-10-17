import scala.language.implicitConversions


object Task2A {
  implicit def voidDefToRunnable(f: () => Unit): Runnable
    = new Runnable { override def run() = f() }

  def makeThread(f: () => Unit): Thread = new Thread(f)
}

object Task2B extends App {
  private var counter: Int = 0

  def increaseCounter(): Unit = { counter += 1 }
  def printCounter(): Unit = { println(counter) }

  var x: Thread = Task2A.makeThread(increaseCounter)
  var y: Thread = Task2A.makeThread(increaseCounter)

  x.start()
  y.start()
  x.join()
  y.join()

  val z = Task2A.makeThread(printCounter)
  z.start()
  z.join()
}

object Task2C extends App {
  private var counter: Int = 0

  def increaseCounter() = {
    counter.synchronized({ counter += 1})
  }
  def printCounter(): Unit = { println(counter) }

  (1 to 5)
  .map(x => {
    val t = Task2A.makeThread(increaseCounter)
    t.start()
    t
  })
  .foreach(t => {
    t.join()
  })

  val z = Task2A.makeThread(printCounter)
  z.start()
  z.join()
}

object Task2D extends App {
  lazy val x: Unit = {
    val t = Task2A.makeThread(() => {
      println("locky locky")
      println(x)
    })
    t.start()
    t.join() // comment to potentially unlocky
    ()
  }
  println(x)
}