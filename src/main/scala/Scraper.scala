import java.net.URL

import akka.actor.{Actor, ActorRef}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

/**
  * Klasa scrapująca dane z stron html
  *
  * @param indexer
  */
class Scraper(indexer: ActorRef) extends Actor {

  def receive: Receive = {
    case Scrap(url) =>
      val startCount = System.nanoTime()
      println(s"Scraping $url")
      val content = parse(url)
      sender() ! ScrapFinished(url)
      val elapsedTime = System.nanoTime() - startCount
      indexer ! Index(url, content, elapsedTime)
  }

  /**
    * Metoda parsująca dane do postaci kontentu.
    *
    * @param url      ścieżka url
    * @return         obiekt Content
    */
  def parse(url: URL): Content = {
    val browser = JsoupBrowser()
    val doc = browser.get(url.toString)

    val title = doc >> texts("title")
    val meta = doc >?> element("meta")
    val postElements = doc >> elementList(".post")

    val posts =  postElements.map { post =>
      val startCount = System.nanoTime()
      val idText = post >?> texts(".click")
      val id = idText.orNull.head.replace("#", "").toLong
      val pointsText = post >?> texts(".points")
      val points = pointsText.orNull.head.toLong
      val content = post >> texts(".post-content")
      val elapsedTime = System.nanoTime() - startCount

      Post(id, points, content.head, elapsedTime)
    }

    Content(title.head, meta.orNull.outerHtml.toString, posts)
  }
}