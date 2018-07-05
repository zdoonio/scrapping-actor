import java.net.URL
import akka.actor.{Actor, ActorRef}

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
    //store.foreach(println)
    println("Scrapping pages saved successful: Exit 0")
    //println(store.size)
  }
}