package basket

import java.util.UUID
import com.twitter.finagle.http.{Response, Request}
import com.twitter.finagle.{Service, Http}
import com.twitter.util.Await

import io.finch._
import io.finch.circe._
import io.circe.generic.auto._

import shapeless.{CNil, :+:}

import scala.collection.mutable



case class Product(fk: UUID, name: String, price: Double)

case class Basket(id: UUID, var productList: mutable.Seq[Product])



sealed trait BasketManagerApi {
  def newBasket: Basket
  def addItem(p: Product): String
  def getBasket(id: UUID): Option[Basket]
  def getBasketByString(s: String): Option[Basket]
}

object BasketManager extends BasketManagerApi {

  private[this] val basketList: mutable.Map[UUID, Basket] = mutable.Map.empty[UUID, Basket]

  def newBasket: Basket = {
    val b = Basket( UUID.randomUUID(), mutable.Seq.empty[Product] )
    basketList += ( b.id -> b)
    b
  }

  override def addItem( p: Product ): String = synchronized {
      val basketOpt: Option[Basket] = basketList.get( p.fk )

      val productsInBasket: Option[mutable.Seq[Product]] = basketOpt match {
        case Some(b)  => Some(b.productList)
        case _        => None
      }

      val result = productsInBasket match {
        case Some(list)  =>   {
          // since we've passed the first for comprehension we can be sure, that
          // the Option has a Some inside ;-)
          // so it's valid to call "get"
          basketOpt.get.productList = list.+:(p)
          "OK"
        }
        case None     =>   s"Basket [${p.fk.toString}] not found"
      }
      result
  }

  override def getBasket( id: UUID ): Option[Basket] = synchronized { basketList.get( id ) }

  override def getBasketByString( uuidString: String ): Option[Basket] = {
    val uuid = UUID.fromString(uuidString)
    getBasket( uuid )
  }
}


object BasketService {

  private def createNewBasket:    Endpoint[Basket] = get( "create"  ) {
    Ok(BasketManager.newBasket)
  }

  private def addItem():            Endpoint[String] = post("add" ?  body.as[Product] ) { p: Product =>
    val result = BasketManager.addItem( p  )
    Ok(result)
 }

  private def showBasket:         Endpoint[Option[Basket]] = get( "show" / string ) {
    uuidString: String => Ok( BasketManager.getBasketByString( uuidString ) )
  }

  def runService() = {
    val api: Service[Request, Response] = (createNewBasket :+: addItem :+: showBasket).toService

    Await.ready(Http.server.serve(":9001", api))
  }
}

