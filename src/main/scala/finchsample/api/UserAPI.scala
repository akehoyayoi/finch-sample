package finchsample.api

import finchsample.model.database.mock.User
import finchsample.model.http.request.UserParams
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

/**
  * Created by okayayohei on 2017/03/04.
  */
trait UserAPI {

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
