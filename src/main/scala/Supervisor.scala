import java.net.URL

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}

class Supervisor extends Actor {
  val indexer = context actorOf Props(new Indexer(self))
  val system = ActorSystem()

  var maxRetries = 2
  var numberVisited = 0
  var globalNumberOfPages = 0
  var toScrap = Set.empty[URL]
  var scrapCounts = Map.empty[URL, Int]
  var host2Actor = Map.empty[String, ActorRef]

  def receive: Receive = {
    case Start(url, numberOfPages) =>
      println(s"Starting scrapping from $url.")
      println(s"Number of pages to scrap: $numberOfPages")
      globalNumberOfPages = numberOfPages
      scrap(url, globalNumberOfPages)

    case ScrapFinished(url) =>
      println(s"Scraping finished $url")

    case IndexFinished(url) =>
      if (numberVisited < globalNumberOfPages)
        checkAndShutdown(url)

    case ScrapFailure(url, reason) =>
      val retries: Int = scrapCounts(url)
      println(s"Scraping failed $url, $retries, reason = $reason")
      if (retries < maxRetries) {
        countVisits(url)
        host2Actor(url.getHost) ! Scrap(url)
      } else
        checkAndShutdown(url)
  }

  def scrap(url: URL, numberOfPages: Int) = {
    println("Start to scrap")
    val host = url.getHost
    println(s"Host = $host")
    if (!host.isEmpty && numberVisited < numberOfPages) {
      val actor = host2Actor.getOrElse(host, {
        val buff = system.actorOf(Props(new SiteCrawler(self, indexer)))
        host2Actor += (host -> buff)
        buff
      })

      numberVisited += 1
      toScrap += url
      countVisits(url)
      actor ! Scrap(url)
    }
  }

  def countVisits(url: URL): Unit = scrapCounts += (url -> (scrapCounts.getOrElse(url, 0) + 1))

  def checkAndShutdown(url: URL): Unit = {
    toScrap -= url

    if (toScrap.isEmpty) {
      self ! PoisonPill
      system.terminate()
    }
  }
}