import java.net.URL
import akka.actor.{Actor, ActorRef}

class Scraper(indexer: ActorRef) extends Actor {

  def receive: Receive = {
    case Scrap(url) =>
      println(s"Scraping $url")
      sender() ! ScrapFinished(url)
      indexer ! Index(url)
  }

  def parse(url: URL): Content = {
    Content("", "", List(), List())
  }
}