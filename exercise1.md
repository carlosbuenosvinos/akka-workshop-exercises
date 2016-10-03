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

class MessageTypeRouter(typeA: ActorRef, typeB: ActorRef, typeC: ActorRef) extends Actor {
  def receive = {
    case x:A => typeA.forward(x)
    case x:B => typeB.forward(x)
    case x:C => typeC.forward(x)
  }
}

object HelloAkkaScala extends App {
  val system = ActorSystem("MyService")

  val typeAProcessor = system.actorOf(Props[TypeAProcessor])
  val typeBProcessor = system.actorOf(Props[TypeBProcessor])
  val typeCProcessor = system.actorOf(Props[TypeCProcessor])
  
  val router = system.actorOf(Props(classOf[MessageTypeRouter], typeAProcessor, typeBProcessor, typeCProcessor))

  router ! A("Joan Miquel")
  router ! B("Christian")
  router ! C("Carlos")

  Thread.sleep(2000L)
  system.stop(router)
  system.terminate()
}
