package finch_hello

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, Http}
import com.twitter.util.Await
import io.finch._

object HelloService2 extends App {

  //--------------------------------------------------
  // Example request:  localhost:9001/hello/peter
  //--------------------------------------------------
  val getHello: Endpoint[String] = get("hello" / string ) {

    name: String => Ok(s"Hello and Welcome $name")
  }

  val api: Service[Request, Response] = getHello.toService

  Await.ready(Http.server.serve(":9001", api ))
}
