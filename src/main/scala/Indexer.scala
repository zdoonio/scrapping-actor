import java.net.URL
import akka.actor.{Actor, ActorRef}
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime

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
    val config = ConfigFactory.load()
    val directory = config.getString("filePath")

    FileWriter.create(store.values.toList, directory + DateTime.now().toString("yyyy-MM-dd") + "-output.txt")
    println("Scrapping pages saved successful: Exit 0")
  }
}