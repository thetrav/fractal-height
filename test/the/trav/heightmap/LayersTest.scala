package the.trav.heightmap

import org.scalatest.{FeatureSpec, GivenWhenThen}
import org.scalatest.matchers.MustMatchers

object LayersTest {
  def main(args:Array[String]) {
    new LayersTest().run()
  }
}

class LayersTest extends MustMatchers {
 def ninePoints = Map(Coord(0,0) -> 1, Coord(1,0)->2, Coord(2,0)->3,
                      Coord(0,1) -> 4, Coord(1,1)->5, Coord(2,1)->6,
                      Coord(0,2) -> 7, Coord(1,2)->8, Coord(2,2)->9)

 //top left
 val neighbors = Map[Direction, Option[(Coord[Int], Int)]](North -> None, East -> Some(Coord(1,0), 2), South-> Some(Coord(0,1), 4), West -> None)
 def run() {

   val coord = Coord(0,0)
   val layer = SingleLayer(ninePoints, 1)

   neighbors.keys.foreach((d:Direction) => {

     val value = neighbors.apply(d)
     println(coord + " should have " + d + " neighbor of "+ value)
     value match {
       case None => {
         layer.neighbor(coord, d) must be === None
       }
       case Some(tuple:(Coord[Int], Int)) => {
         layer.neighbor(coord, d).get must be === tuple._1
         layer.point(tuple._1).get must be === tuple._2
       }
     }
   })
 }

// feature("A single layer should provide the correct neighbors") {
//   info("As a programmer")
//   info("I want to be able to navigate around a single layer")
//   info("So I can build sliding windows for midpoint displacement")
//
//   scenario("top left corner east neighbor") {
//     given("a single layer of points ({1,2,3},{4,5,6},{7,8,9})")
//     val layer = SingleLayer[Int](ninePoints, 1)
//      and("we have the north western point which is Coord(0,0)")
//     val c = Coord(0,0)
//     when("we ask for the east neighbor")
//     val neighbor = layer.neighbor(c, East)
//     then("we should be given the correct coord which is Some(Coord(1,0))")
//     assert(neighbor == Some(Coord(1,0)))
//      and("the point for that neighbor should be 2")
//     val neighborPoint = layer.point(neighbor.get)
//      neighborPoint must be === Some(2)
//   }
//
//   scenario("top left corner south neighbor")
//    given("a single 3x3 layer of points ({1,2,3},{4,5,6},{7,8,9})")
//     and("we have the north west point which is Coord(0,0)")
//    when("we ask for the south neighbor")
//    then("we should be given the correct coord which is Some(Coord(0,1))")
//     and("the point for that neighbor should be 4")
//
//   scenario("top left corner west neighbor")
//    given("a single 3x3 layer of points ({1,2,3},{4,5,6},{7,8,9})")
//     and("we have the north west point which is Coord(0,0)")
//    when("we ask for the west neighbor")
//    then("we should be given None")
//
//   scenario("top left corner north neighbor")
//    given("a single 3x3 layer of points ({1,2,3},{4,5,6},{7,8,9})")
//     and("we have the north west point which is Coord(0,0)")
//    when("we ask for the north neighbor")
//    then("we should be given None")
//  }

}