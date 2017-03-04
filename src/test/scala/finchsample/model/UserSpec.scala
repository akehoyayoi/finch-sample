package finchsample.model

import com.twitter.util.Await
import org.specs2.mutable.Specification

/**
  * Created by okayayohei on 2017/02/28.
  */
class UserSpec extends Specification {
  "user" should {
    "create ~ create ~ find ~ all" in {
      val firstId = User.create("abc@abc","abc")

      Await.result(firstId).id must_=== 0L

      val secondId = User.create("def@def", "def")

      Await.result(secondId).id must_=== 1L

      val findUser = User.find(0L)
      val userOp = Await.result(findUser)
      userOp must not be None
      val user = userOp.get
      user.id must_=== 0L
      user.email must_=== "abc@abc"
      user.name must_=== "abc"

      val allUsers = User.all()
      val users = Await.result(allUsers)
      users.size must_=== 2
      val sortedUsers = users.sortBy(_.id)

      val firstUser = sortedUsers(0)
      firstUser.id must_=== 0L
      firstUser.email must_=== "abc@abc"
      firstUser.name must_=== "abc"

      val secondUser = sortedUsers(1)
      secondUser.id must_=== 1L
      secondUser.email must_=== "def@def"
      secondUser.name must_=== "def"
    }

  }
}
