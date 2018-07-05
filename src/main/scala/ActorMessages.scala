import java.net.URL

case class Start(url: URL, numberOfPages: Int)
case class Scrap(url: URL)
case class Index(url: URL)
case class ScrapFinished(url: URL)
case class IndexFinished(url: URL)
case class ScrapFailure(url: URL, reason: Throwable)
case class Content(title: String, meta: String, urls: List[URL], posts: List[Post])
case class Post(id: Long, points: Long, content: String)