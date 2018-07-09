import play.api.libs.json.{JsValue, Json, Writes}

/**
  * Obiekt do mapowania danych do postaci JSON
  */
object JsonWriter {

  implicit val writes_format = Json.format[Post]

  implicit val Post_Writes: Writes[Post] = new Writes[Post] {
    def writes(post: Post): JsValue =
      Json.obj(
        "id" -> post.id,
        "points" -> post.points,
        "content" -> post.content
      )
  }
}
