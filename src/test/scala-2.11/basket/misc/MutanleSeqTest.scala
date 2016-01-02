package basket.misc

import scala.collection.mutable


case class Product( name: String, price: Double)

object MutanleSeqTest extends App {

  val list = mutable.Seq.empty[Product]

  val p1 = Product( "T-Shirt", 23.50)
  val p2 = Product( "Mug", 2.25)
  val p3 = Product( "Jeans", 110.00)

  println( list.size )
  val l = list :+ p1
  println( l.size )

  val l2 = l :+ p1
  println( l2.size )

  println( list.size )



}
