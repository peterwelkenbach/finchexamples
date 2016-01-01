package finch_hello

import com.twitter.finagle.Http
import com.twitter.util.Await

import io.finch._
import io.finch.circe._
import io.circe.generic.auto._


object HelloService extends App {

   //--------------------------------------------------
   // Example request:  localhost:9001/hello/peter
   //--------------------------------------------------
   val e: Endpoint[String] = get("hello" / string ) {

     name: String => Ok(s"Hello and Welcome $name")
   }


  Await.ready(Http.server.serve(":9001", e.toService ))
}
