import java.io.PrintWriter
import JsonWriter.Post_Writes
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Obiekt do zapisu danych na dysku
  */
object FileWriter {

  /**
    * Metoda tworzaca nowy plik na dysku
    *
    * @param contents     lista zescrapowanych danych
    * @param filePath     ścieżka do zapisu
    * @return             opis błędu w postaci stringa jeżeli zapis się nie powiedzie
    */
  def create(contents: List[Content], filePath: String): Future[Option[String]] = {
    val file = new PrintWriter(filePath, "UTF-8")
    val postsFromContents = contents.flatten(_.posts.map {post => post})
    val parsePostsToJson = Json.obj("posts" -> postsFromContents)

    println("Writes data at location: " + filePath)
    try {
      file.print(parsePostsToJson.toString)
      file.close()
      Future(None)

    } catch {
      case e => Future(Some(e.toString))

    }
  }
}
