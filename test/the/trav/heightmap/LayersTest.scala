package the.trav.heightmap

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

  def run() {
   println("testing single layer")
   val layer = SingleLayer(ninePoints, 1)

   println("testing north west corner")
   testSingleLayer(layer, Coord(0,0), Map( North -> None,
                                           East -> Some(Coord(1,0), 2), 
                                           South-> Some(Coord(0,1), 4),
                                           West -> None))

   println("testing north edge")
   testSingleLayer(layer, Coord(1,0), Map( North -> None,
                                           East -> Some(Coord(2,0), 3),
                                           South-> Some(Coord(1,1), 5),
                                           West -> Some(Coord(0,0), 1)))

   println("testing north east corner")
   testSingleLayer(layer, Coord(2,0), Map( North -> None,
                                           East -> None,
                                           South-> Some(Coord(2,1), 6),
                                           West -> Some(Coord(1,0), 2)))

   println("testing west edge")
   testSingleLayer(layer, Coord(0,1), Map( North -> Some(Coord(0,0), 1),
                                           East -> Some(Coord(1,1), 5),
                                           South-> Some(Coord(0,2), 7),
                                           West -> None))

   println("testing center")
   testSingleLayer(layer, Coord(1,1), Map( North -> Some(Coord(1,0), 2),
                                           East -> Some(Coord(2,1), 6),
                                           South-> Some(Coord(1,2), 8),
                                           West -> Some(Coord(0,1), 4)))

   println("testing east edge")
   testSingleLayer(layer, Coord(2,1), Map( North -> Some(Coord(2,0), 3),
                                           East -> None,
                                           South-> Some(Coord(2,2), 9),
                                           West -> Some(Coord(1,1), 5)))

   println("testing south west corner")
   testSingleLayer(layer, Coord(0,2), Map( North -> Some(Coord(0,1), 4),
                                           East -> Some(Coord(1,2), 8),
                                           South-> None,
                                           West -> None))

   println("testing south edge")
   testSingleLayer(layer, Coord(1,2), Map( North -> Some(Coord(1,1), 5),
                                           East -> Some(Coord(2,2), 9),
                                           South-> None,
                                           West -> Some(Coord(0,2), 7)))


   println("testing south east corner")
   testSingleLayer(layer, Coord(2,2), Map( North -> Some(Coord(2,1), 6),
                                           East -> None,
                                           South-> None,
                                           West -> Some(Coord(1,2), 8)))
   println("all tests passed")
 }

  def testSingleLayer(layer:SingleLayer[Int], coord:Coord[Int], neighbors:Map[Direction, Option[(Coord[Int], Int)]]) {
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
      println("passed")
   })
  }
}