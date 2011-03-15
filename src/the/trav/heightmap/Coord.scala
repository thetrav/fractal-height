package the.trav.heightmap

case class Coord(x:Double, y:Double) {
  def + (c:Coord) = Coord(x+c.x, y+c.y)

  def step(d:Direction, scale:Double) = d match {
    case North => Coord(x, y-scale)
    case East => Coord(x+scale, y)
    case South => Coord(x, y+scale)
    case West => Coord(x-scale, y)
  }
}