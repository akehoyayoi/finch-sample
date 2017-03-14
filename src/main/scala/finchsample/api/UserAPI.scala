package finchsample.api

import finchsample.model.database.mysql.User
import finchsample.model.http.request.UserParams
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import com.twitter.finagle.mysql.Client
/**
  * Created by okayayohei on 2017/03/04.
  */
trait UserAPI {

  implicit def client: Client

  // cannot use private
  val createUser: Endpoint[User] = post("users" :: jsonBody[UserParams]) { userParams: UserParams =>
    User.create(userParams.email, userParams.name).map { user =>
      Ok(user)
    }
  }

  val showUser: Endpoint[Seq[User]] = get("users") {
    User.all().map { users =>
      Ok(users)
    }
  }

  val findUser: Endpoint[User] = get("users" :: long) { id: Long =>
    User.find(id).map {
      case Some(user) => Ok(user)
      case None => NotFound(new Exception("Record Not Found"))
    }
  }

  val userAPI = createUser :+: showUser :+: findUser
}
