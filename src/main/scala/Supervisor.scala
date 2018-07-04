import java.net.URL

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class Supervisor extends Actor {
  val indexer = context actorOf Props(new Indexer(self))
  val system = ActorSystem()

  var numVisited = 0
  var toScrap = Set.empty[URL]
  var scrapCounts = Map.empty[URL, Int]
  var host2Actor = Map.empty[String, ActorRef]

  def receive: Receive = {
    case Start(url, pageNumber) =>
      println(s"Starting scrapping from $url.")
      println(s"Number of pages to scrap: $pageNumber")
      scrap(url)
  }

  def scrap(url: URL) = {
    println("Start to scrap")
    val host = url.getHost
    println(s"host = $host")
    if (!host.isEmpty) {
      val actor = host2Actor.getOrElse(host, {
        val buff = system.actorOf(Props(new SiteCrawler(self, indexer)))
        host2Actor += (host -> buff)
        buff
      })

      numVisited += 1
      toScrap += url
      countVisits(url)
      actor ! Scrap(url)
    }
  }

  def countVisits(url: URL): Unit = scrapCounts += (url -> (scrapCounts.getOrElse(url, 0) + 1))
}