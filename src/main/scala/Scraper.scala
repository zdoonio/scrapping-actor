import java.net.URL

import akka.actor.{Actor, ActorRef}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._


class Scraper(indexer: ActorRef) extends Actor {

  def receive: Receive = {
    case Scrap(url) =>
      println(s"Scraping $url")
      val content = parse(url)
      sender() ! ScrapFinished(url)
      indexer ! Index(url, content)
  }

  def parse(url: URL): Content = {
    val browser = JsoupBrowser()
    val doc = browser.get(url.toString)

    val title = doc >> texts("title")
    val meta = doc >?> element("meta")
    val postElements = doc >> elementList(".post")

    val posts =  postElements.map { post =>
      val idText = post >?> texts(".click")
      val id = idText.orNull.head.replace("#", "").toLong
      val pointsText = post >?> texts(".points")
      val points = pointsText.orNull.head.toLong
      val content = post >> texts(".post-content")

      Post(id, points, content.head)
    }

    Content(title.head, meta.orNull.outerHtml.toString, posts)
  }
}