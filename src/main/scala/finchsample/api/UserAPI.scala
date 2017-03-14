package finchsample.api

import com.twitter.finagle.Mysql
import finchsample.model.database.common
import finchsample.model.database.mysql.Users
import finchsample.model.http.request.UserParams
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

/**
  * Created by okayayohei on 2017/03/04.
  */

trait UserAPI {

  val accessor: Users = new Users(Mysql.client
    .withCredentials(
      sys.env.getOrElse("MYSQL_USER", "user"),
      sys.env.getOrElse("MYSQL_PASSWORD", "password"))
    .withDatabase(
      sys.env.getOrElse("MYSQL_DATABASE", "database"))
    .newRichClient(
      sys.env.getOrElse("MYSQL_ADDRESS", "127.0.0.1")
        + ":" + sys.env.getOrElse("MYSQL_PORT", "3306")))

  // cannot use private
  val createUser: Endpoint[common.User] = post("users" :: jsonBody[UserParams]) { userParams: UserParams =>
    accessor.create(userParams.email, userParams.name).map { user =>
      Ok(user)
    }
  }

  val showUser: Endpoint[Seq[common.User]] = get("users") {
    accessor.all().map { users =>
      Ok(users)
    }
  }

  val findUser: Endpoint[common.User] = get("users" :: long) { id: Long =>
    accessor.find(id).map {
      case Some(user) => Ok(user)
      case None => NotFound(new Exception("Record Not Found"))
    }
  }

  val userAPI = createUser :+: showUser :+: findUser
}
