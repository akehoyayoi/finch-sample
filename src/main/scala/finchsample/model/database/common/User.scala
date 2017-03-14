package finchsample.model.database.common

import argonaut.Argonaut.casecodec3
import argonaut.CodecJson
import com.twitter.util.Future

/**
  * Created by okayayohei on 2017/03/14.
  */

trait Users {
  def all(): Future[Seq[User]]
  def find(id: Long): Future[Option[User]]
  def create(email:String, name: String): Future[User]
}


case class User(id: Long, email: String, name: String)

object User {
  implicit val userCodec: CodecJson[User] =
    casecodec3(User.apply, User.unapply)("id", "email", "name")
}
