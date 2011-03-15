package the.trav.heightmap

object TestNeighbors {

  case object east extends Coord(-1,0)
  case object north extends Coord(0,1)
  case object west extends Coord(1,0)
  case object south extends Coord(0,1)

  def step(c:Coord, d:Direction) = d match {
    case East => east
    case North => north
    case West => west
    case South => south
  }

  def main(args:Array[String]) {
    def t(coord:Coord, expected:Set[Coord]) = test(coord, expected, 2)
    t(Coord(1,0), Set(east, west, south))
    t(Coord(0,1), Set(east, north, south))
    t(Coord(1,1), Set(north, east, west, south))
    t(Coord(2,1), Set(north, west, south))
    t(Coord(1,2), Set(north, west, east))
  }

  def test(coord:Coord, expected:Set[Coord], squareSize:Int) {
    val neighbors = MappedProgressive.squareNeighbors(coord, step, squareSize)
    println("coord:"+ coord + " has " + neighbors + " should == " +expected)
    assert(neighbors == expected)
    println("passed")
  }
}