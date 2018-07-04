import akka.actor.{Actor, ActorRef}

class Scraper(indexer: ActorRef) extends Actor {

  def receive: Receive = {
    case Scrap(url) =>
      println(s"Scraping $url")
  }
}