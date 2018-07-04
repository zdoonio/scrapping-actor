import akka.actor.{Actor, ActorRef}

class SiteCrawler(supervisor: ActorRef, indexer: ActorRef) extends Actor {

  def receive: Receive = {
    case Scrap(url) =>
      println(s"Waiting... $url")
  }
}
