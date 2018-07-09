import java.net.URL
import akka.actor.{Actor, ActorRef, PoisonPill}
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime
import scala.concurrent.duration._
import scala.concurrent.Await

class ContentSaver extends Actor {

  def receive: Receive = {
    case ContentToSave(posts) =>
      println(s"Starting saving process...")
      saveContentToFile(posts)
  }

  def saveContentToFile(content: List[Content]) = {
    val config = ConfigFactory.load()
    val directory = config.getString("filePath")
    val savingResult = Await.result(FileWriter.create(content, directory + DateTime.now().toString("yyyy-MM-dd") + "-output.txt"), 10 seconds)

    if(savingResult.isEmpty)
      println("Scraped content saved successful: Exit 0")
    else
      println("Some problems with saving file. Exit 0")

    self ! PoisonPill

  }

}