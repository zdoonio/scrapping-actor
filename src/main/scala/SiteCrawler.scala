import java.net.URL

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

class SiteCrawler(supervisor: ActorRef, indexer: ActorRef) extends Actor {
  val process = "Process next url"

  val scraper = context actorOf Props(new Scraper(indexer))
  implicit val timeout = Timeout(3 seconds)
  val tick = context.system.scheduler.schedule(0 millis, 1000 millis, self, process)
  var toProcess = List.empty[URL]

  def receive: Receive = {
    case Scrap(url) =>
      println(s"Waiting... $url")
      toProcess = url :: toProcess

    case `process` =>
      toProcess match {
        case Nil =>

        case url :: list =>
          println(s"Site scraping... $url")
          toProcess = list
          (scraper ? Scrap(url)).mapTo[ScrapFinished]
            .recoverWith { case e => Future {ScrapFailure(url, e)} }
            .pipeTo(supervisor)
      }
  }

}
