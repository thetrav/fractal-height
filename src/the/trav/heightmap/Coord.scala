package the.trav.heightmap

import Numeric._

case class Coord[T](x:T, y:T)(implicit numeric: Numeric[T]) {
  def + (c:Coord[T]) = Coord[T](numeric.plus(x, c.x), numeric.plus(y, c.y))
}