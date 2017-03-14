import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.Mysql
import com.twitter.util.Await
import finchsample.api.{EchoAPI, UserAPI}
import io.circe.generic.auto._
import io.finch.circe._

/**
  * Created by okayayohei on 2017/03/04.
  */
object Main extends UserAPI with EchoAPI {

  implicit val client = Mysql.client
    .withCredentials(
      sys.env.getOrElse("MYSQL_USER", "user"),
      sys.env.getOrElse("MYSQL_PASSWORD", "password"))
    .withDatabase(
      sys.env.getOrElse("MYSQL_DATABASE", "database"))
    .newRichClient(
      sys.env.getOrElse("MYSQL_ADDRESS", "127.0.0.1")
        + ":" + sys.env.getOrElse("MYSQL_PORT", "3306"))

  val api: Service[Request, Response] = (userAPI :+: echoAPI) toService

  def main(args: Array[String]): Unit = {
    Await.ready(Http.serve(":8080", api))
  }
}
