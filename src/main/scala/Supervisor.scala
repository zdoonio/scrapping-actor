import java.net.URL

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}

/**
  * Klasa supervisora odpowiedzialna za kontrolowanie poszczególnych podrzędnych aktorów
  */
class Supervisor extends Actor {
  val indexer = context actorOf Props(new Indexer(self))
  val system = ActorSystem()

  var maxRetries = 2
  var numberVisited = 0
  var globalNumberOfPages = 0
  var globalUrl: URL = _
  var toScrap = Set.empty[URL]
  var scrapCounts = Map.empty[URL, Int]
  var host2Actor = Map.empty[String, ActorRef]

  def receive: Receive = {
    case Start(url, numberOfPages) =>
      println(s"Starting scrapping from $url.")
      println(s"Number of pages to scrap: $numberOfPages")
      globalNumberOfPages = numberOfPages
      globalUrl = url
      scrap(url, globalNumberOfPages)

    case Continue(url, numberOfPages) =>
      println(s"Starting scrapping from $url.")
      println(s"Number of pages to scrap: $numberOfPages")
      scrap(url, globalNumberOfPages)

    case ScrapFinished(url) =>
      println(s"Scraping finished $url")
      self ! Continue(globalUrl, globalNumberOfPages - numberVisited)

    case IndexFinished(url, content) =>
      if (numberVisited == globalNumberOfPages)
        println("Scrapping process finishing...")
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

  /**
    * Metoda scrapująca dane z url
    *
    * @param url              adres url
    * @param numberOfPages    ilość pozostałych stron do zescrapowania
    */
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
      val actualUrl = new URL(url.toString + s"?page=$numberVisited")
      toScrap += actualUrl
      countVisits(actualUrl)
      actor ! Scrap(actualUrl)
    }
  }

  /**
    * Metoda odpowiedzialna za liczenie wizyt na hoscie
    *
    * @param url    ścieżka url
    */
  def countVisits(url: URL): Unit = scrapCounts += (url -> (scrapCounts.getOrElse(url, 0) + 1))

  /**
    * Metoda sprawdzająca czy należy przerwać proces supervisora jeżeli nie odejmuj adresy url do zescrapowania
    *
    * @param url    ścieżka url
    */
  def checkAndShutdown(url: URL): Unit = {
    toScrap -= url

    if (toScrap.isEmpty) {
      self ! PoisonPill
      system.terminate()
    }
  }
}