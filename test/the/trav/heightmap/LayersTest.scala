package the.trav.heightmap

import org.scalatest.{FeatureSpec, GivenWhenThen}


class LayersTest extends FeatureSpec with GivenWhenThen {
 def ninePoints = Map(Coord(0,0) -> 1, Coord(1,0)->2, Coord(2,0)->3,
                      Coord(0,1) -> 4, Coord(1,1)->5, Coord(2,1)->6,
                      Coord(0,2) -> 7, Coord(1,2)->8, Coord(2,2)->9)
 feature("A single layer should provide the correct neighbors") {
   info("As a programmer")
   info("I want to be able to navigate around a single layer")
   info("So I can build sliding windows for midpoint displacement")

   scenario("top left corner") {
     given("a single layer of points ({1,2,3},{4,5,6},{7,8,9})")
     val layer = SingleLayer[Int](ninePoints, 1)
      and("we have the north western point which is Coord(0,0)")
     val c = Coord(0,0)
     when("we ask for the east neighbor")
     val neighbor = layer.neighbor(c, East)
     then("we should be given the correct coord which is Some(1,0)")
     assert(neighbor == Some(Coord(1,0)))
      and("we the point for that neighbor should be 2")
      assert(layer.point(neighbor.get) == Some(2))
   }
    //given a single 3x3 layer of points ({1,2,3},{4,5,6},{7,8,9})
    // and we have the north western point (1)
    //When we ask for the south neighbor
    //Then we should be given the correct point which is Some(4)

    //given a single 3x3 layer of points ({1,2,3},{4,5,6},{7,8,9})
    // and we have the north western point (1)
    //When we ask for the west neighbor
    //Then we should be given the correct point which is None

    //given a single 3x3 layer of points ({1,2,3},{4,5,6},{7,8,9})
    // and we have the north western point (1)
    //When we ask for the north neighbor
    //Then we should be given the correct point which is None
  }
}