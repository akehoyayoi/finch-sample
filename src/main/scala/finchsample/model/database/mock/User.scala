package finchsample.model.database.mock

import com.twitter.util.Future
import finchsample.model.database.common
import scala.collection.mutable

/**
  * Created by okayayohei on 2017/02/28.
  */

class Users extends common.Users {
  def all() = User.all()
  def find(id: Long) = User.find(id)
  def create(email:String, name: String) = User.create(email, name)
}

object User {

  private[this] val users: mutable.Map[Long, common.User] = mutable.Map.empty[Long, common.User]

  def all(): Future[Seq[common.User]] = Future {
    users.synchronized {
      users.values.toSeq
    }
  }

  def find(id: Long): Future[Option[common.User]] = Future {
    users.synchronized {
      users.get(id)
    }
  }

  def create(email: String, name: String): Future[common.User] = Future {
    users.synchronized {
      val id = if (users.isEmpty) 0 else users.keys.max + 1
      users(id) = common.User(id, email, name)
      users(id)
    }
  }


}
