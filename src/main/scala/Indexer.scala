import java.net.URL
import akka.actor.{Actor, ActorRef}

class Indexer(supervisor: ActorRef) extends Actor {
  var store = Map.empty[URL, Content]
  var content = Map.empty[URL, Content]

  def receive: Receive = {
    case Index(url) =>
      println(s"Saving from $url")
      supervisor ! IndexFinished(url)
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
  }
}