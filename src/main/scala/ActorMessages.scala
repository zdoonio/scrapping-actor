import java.net.URL

case class Start(url: URL, pageNumber: Int)
case class Scrap(url: URL)
case class Index(url: URL)
case class ScrapFinished(url: URL)
case class IndexFinished(url: URL, urls: List[URL])
case class ScrapFailure(url: URL, reason: Throwable)