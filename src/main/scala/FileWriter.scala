import java.io.PrintWriter
import JsonWriter.Post_Writes
import play.api.libs.json.Json

object FileWriter {
  def create(contents: List[Content], filePath: String) = {
    val file = new PrintWriter(filePath, "UTF-8")
    val postsFromContents = contents.flatten(_.posts.map {post => post})
    val parsePostsToJson = Json.obj("posts" -> postsFromContents)

    println("Writes data at location: " + filePath)
    file.write(parsePostsToJson.toString)
  }
}
