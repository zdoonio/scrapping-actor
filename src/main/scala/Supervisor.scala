import java.net.URL
import akka.actor.{Actor, ActorSystem}

class Supervisor extends Actor {

  def receive: Receive = {
    case Start(url, pageNumber) =>
      println(s"Starting scraping from $url.")
      println(s"Number of pages to scrap: $pageNumber")
      scrap(url)
  }

  def scrap(url: URL) = {
    println("Started scrapping")
  }
}