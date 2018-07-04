import akka.actor.{Actor, ActorRef}

class Indexer(supervisor: ActorRef) extends Actor {

  def receive: Receive = {
    case Index(url) =>
      println(s"saving page $url")
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
  }
}