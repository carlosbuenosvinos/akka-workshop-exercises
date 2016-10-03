import akka.actor._

case class A(message: String)
case class B(message: String)
case class C(message: String)

class TypeAProcessor extends Actor {
  def receive = {
    case a: A => println(s"Echo from A: ${a.message}")
  }
}

class TypeBProcessor extends Actor {
  def receive = {
    case b: B => println(s"Echo from B: ${b.message}")
  }
}

class TypeCProcessor extends Actor {
  def receive = { 
    case c: C => println(s"Echo from C: ${c.message}")
  }
}

class MessageTypeRouter extends Actor {
  val typeA = context.actorOf(Props[TypeAProcessor])
  val typeB = context.actorOf(Props[TypeBProcessor])
  val typeC = context.actorOf(Props[TypeCProcessor])
  
  def receive = {
    case x:A => typeA.forward(x)
    case x:B => typeB.forward(x)
    case x:C => typeC.forward(x)
  }
}

object HelloAkkaScala extends App {
  val system = ActorSystem("MyService")
  val router = system.actorOf(Props(classOf[MessageTypeRouter]))

  router ! A("Joan Miquel")
  router ! B("Christian")
  router ! C("Carlos")

  Thread.sleep(2000L)
  system.stop(router)
  system.terminate()
}
