package the.trav.heightmap

import TypeShortcuts.PointMap
import Numeric._

trait Layer[T] {
  val scale:T
  val points:PointMap[T]
  def point(c:Coord[T]) = points.get(c)
  def neighbor(c:Coord[T], d:Direction)(implicit numeric:Numeric[T]):Option[Coord[T]] = {
    val target = d match {
      case North => c + Coord[T](numeric.zero, numeric.negate(scale))
      case South => c + Coord[T](numeric.zero, scale)
      case East  => c + Coord[T](numeric.zero, scale)
      case West  => c + Coord[T](numeric.zero, numeric.negate(scale))
    }
    if(points.contains(target)) Some(target) else None
  }
}

case class SingleLayer[T](points:PointMap[T], scale:T) extends Layer[T]
case class TopLayer[T](points:PointMap[T], below:Layer[T], scale:T) extends Layer[T]
case class MiddleLayer[T](points:PointMap[T], above:Layer[T], below:Layer[T], scale:T) extends Layer[T]
case class BottomLayer[T](points:PointMap[T], above:Layer[T], scale:T) extends Layer[T]