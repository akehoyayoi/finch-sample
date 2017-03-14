package finchsample.model.database.mysql

import com.twitter.finagle.mysql._
import com.twitter.util.Future
import finchsample.model.database.common

/**
  * Created by okayayohei on 2017/03/07.
  */

class Users(val client: Client) extends common.Users {
  def all() = User.all(client)()
  def find(id: Long) = User.find(client)(id)
  def create(email:String, name: String) = User.create(client)(email, name)
}

object User {

  def all(client: Client)(): Future[Seq[common.User]] =
    client.select("SELECT * FROM users")(convertToEntity)

  def find(client: Client)(id: Long): Future[Option[common.User]] =
    client.prepare("SELECT * FROM users WHERE id = ?")(id) map { result =>
      result.asInstanceOf[ResultSet].rows.map(convertToEntity).headOption
    }

  def create(client: Client)(email: String, name: String): Future[common.User] =
    client.prepare("INSERT INTO users (email, name) VALUES(?, ?)")(email, name) map { result =>
      val id = result.asInstanceOf[OK].insertId
      common.User(id, email, name)
    }

  def convertToEntity(row: Row): common.User = {
    val LongValue(id)       = row("id").get
    val StringValue(email)  = row("email").get
    val StringValue(name)   = row("name").get
    common.User(id, email, name)
  }
}
