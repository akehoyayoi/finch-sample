package finchsample.model.database.mysql

import argonaut.Argonaut._
import argonaut.CodecJson
import com.twitter.finagle.mysql._
import com.twitter.util.Future

/**
  * Created by okayayohei on 2017/03/07.
  */
case class User(id: Long, email: String, name: String)

object User {

  implicit val userCodec: CodecJson[User] =
    casecodec3(User.apply, User.unapply)("id", "email", "name")

  def all()(implicit client: Client): Future[Seq[User]] =
    client.select("SELECT * FROM users")(convertToEntity)

  def find(id: Long)(implicit client: Client): Future[Option[User]] =
    client.prepare("SELECT * FROM users WHERE id = ?")(id) map { result =>
      result.asInstanceOf[ResultSet].rows.map(convertToEntity).headOption
    }

  def create(email: String, name: String)(implicit client: Client): Future[User] =
    client.prepare("INSERT INTO users (email, name) VALUES(?, ?)")(email, name) map { result =>
      val id = result.asInstanceOf[OK].insertId
      User(id, email, name)
    }

  def convertToEntity(row: Row): User = {
    val LongValue(id)            = row("id").get
    val StringValue(email)       = row("email").get
    val StringValue(screen_name) = row("screen_name").get

    User(id, email, screen_name)
  }
}
