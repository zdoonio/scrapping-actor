import java.net.URL

case class Start(url: URL, numberOfPages: Int)
case class Continue(url: URL, numberOfPages: Int)
case class Scrap(url: URL)
case class Index(url: URL, content: Content)
case class ScrapFinished(url: URL)
case class IndexFinished(url: URL, content: Content)
case class ScrapFailure(url: URL, reason: Throwable)
case class Content(title: String, meta: String, posts: List[Post])
case class Post(id: Long, points: Long, content: String)
case class ContentToSave(posts: List[Content])