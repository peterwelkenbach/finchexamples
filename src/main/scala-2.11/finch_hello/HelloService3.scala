package finch_hello


import com.twitter.finagle.http.{Response, Request}
import com.twitter.finagle.{Service, Http}
import com.twitter.util.Await

import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import shapeless.{CNil, :+:}

case class Foo( f: String )
case class Bar( b: String )
case class Person( firstname: String, lastname: String )

object HelloService3 extends App {

  val foo: Endpoint[Foo] = get("foo" / string ) {
    name: String => Ok( Foo(name))
  }

  val bar: Endpoint[Bar] = get("bar" / string ) {
    name: String => Ok( Bar(name))
  }


  val person: Endpoint[Person] = get("person" / string ) {
    name: String => Ok( Person( name , "Welkenbach"))
  }


  val foobar: Endpoint[ Foo :+: Bar :+: Person :+: CNil ] = foo :+: bar :+: person

  val api: Service[Request, Response] = foobar.toService

  Await.ready(Http.server.serve(":9001", api ))
}
