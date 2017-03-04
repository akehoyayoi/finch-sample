package finchsample.model

import com.twitter.util.Future

import scala.collection.mutable

import argonaut.Argonaut._
import argonaut.CodecJson

/**
  * Created by okayayohei on 2017/02/28.
  */
case class User(id: Long, email: String, name: String)

object User {

  implicit val userCodec: CodecJson[User] =
    casecodec3(User.apply, User.unapply)("id", "email", "name")

  private[this] val users: mutable.Map[Long, User] = mutable.Map.empty[Long, User]

  def all(): Future[Seq[User]] = Future {
    users.synchronized {
      users.values.toSeq
    }
  }


  def find(id: Long): Future[Option[User]] = Future {
    users.synchronized {
      users.get(id)
    }
  }

  def create(email: String, name: String): Future[User] = Future {
    users.synchronized {
      val id = if (users.isEmpty) 0 else users.keys.max + 1
      users(id) = User(id, email, name)
      users(id)
    }
  }


}
