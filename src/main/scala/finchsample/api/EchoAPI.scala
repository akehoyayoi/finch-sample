package finchsample.api

import io.finch._

/**
  * Created by okayayohei on 2017/03/04.
  */
trait EchoAPI {

  val echo: Endpoint[String] = get("echo" :: string) { (phrase: String) =>
    Ok(phrase)
  }

  val echoAPI = echo
}
