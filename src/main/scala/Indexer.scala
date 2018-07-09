import java.net.URL
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime

import scala.concurrent.duration._
import scala.concurrent.Await

class Indexer(supervisor: ActorRef) extends Actor {
  var store = Map.empty[URL, Content]
  var content = Map.empty[URL, Content]

  def receive: Receive = {
    case Index(url, content) =>
      println(s"Saving from $url with $content")
      store += (url -> content)
      supervisor ! IndexFinished(url, content)
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    val system = ActorSystem()
    val contentSaver = system.actorOf(Props[ContentSaver], "contentSaver")

    contentSaver ! ContentToSave(store.values.toList)
  }
}