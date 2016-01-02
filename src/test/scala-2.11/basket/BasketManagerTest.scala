package basket

import java.util.UUID

object BasketManagerTest extends App {
  import basket._



  val myBasket1 = BasketManager.newBasket

  val uuid_1 = myBasket1.id

  println( uuid_1 )

  val p1 = Product( uuid_1, "T-Shirt", 23.50)
  val p2 = Product( uuid_1, "Mug", 2.25)
  val p3 = Product( uuid_1, "Jeans", 110.00)


  println( myBasket1.productList.size  )


  println( BasketManager.addItem( p1  )   )
  println( BasketManager.addItem( p2  )   )
  println( BasketManager.addItem( p3  )   )

  val other = BasketManager.getBasket( uuid_1 )
  if (other.isDefined)
    println( other.get.productList.size )

  val uuid_2 = UUID.randomUUID()
  val other2 = BasketManager.getBasket( uuid_2 )
  println(  other2.getOrElse("Basket not found")   )

}
