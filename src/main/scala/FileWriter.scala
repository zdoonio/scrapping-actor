import java.io.PrintWriter
import JsonWriter.Post_Writes
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FileWriter {
  def create(contents: List[Content], filePath: String): Future[Option[String]] = {
    val file = new PrintWriter(filePath, "UTF-8")
    val postsFromContents = contents.flatten(_.posts.map {post => post})
    val parsePostsToJson = Json.obj("posts" -> postsFromContents)

    println("Writes data at location: " + filePath)
    try {
      file.write(parsePostsToJson.toString)
      Future(None)

    } catch {
      case e => Future(Some(e.toString))

    }
  }
}
