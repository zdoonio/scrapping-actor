import java.net.URL
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime

import scala.concurrent.duration._
import scala.concurrent.Await

class Indexer(supervisor: ActorRef) extends Actor {
  var store = Map.empty[URL, Content]
  var content = Map.empty[URL, Content]
  var storeTime = Map.empty[URL, Long]

  def receive: Receive = {
    case Index(url, content, elapsedTime) =>
      println(s"Saving from $url with $content")
      store += (url -> content)
      storeTime += (url -> elapsedTime)
      supervisor ! IndexFinished(url, content)
  }

  /**
    * Metoda licząca i wyświetlająca statystyki.
    */
  def showStatistics = {
    var postTimeAdder: Long = 0
    var pageTimerAdder: Long = 0

    val postsFromContents = store.values.toList.flatten(_.posts.map { post =>
      postTimeAdder += post.elapsedTime
      post
    })

    storeTime.values.foreach { time =>
      pageTimerAdder += time
    }

    val avarageTimePost = postTimeAdder / (postsFromContents.size * 1000000.0)
    val avarageTimeContent = pageTimerAdder / (storeTime.size * 1000000.0)

    println(s"Number of getted posts: ${postsFromContents.size}, " +
      s"avarage time to get post: ${BigDecimal(avarageTimePost).setScale(4, BigDecimal.RoundingMode.HALF_UP).toDouble} ms, " +
      s"avarage time to get page: ${BigDecimal(avarageTimeContent).setScale(4, BigDecimal.RoundingMode.HALF_UP).toDouble} ms.")

  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    val system = ActorSystem()
    val contentSaver = system.actorOf(Props[ContentSaver], "contentSaver")

    contentSaver ! ContentToSave(store.values.toList)

    showStatistics
  }
}