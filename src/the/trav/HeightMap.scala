package the.trav

import java.util.Random

case class Coord(x:Int, y:Int)

object HeightMap {
  def main(args:Array[String]) {
    println("hello world")
  }
}

trait Node {
  def east():Option[Node]
  def west():Option[Node]
  def north():Option[Node]
  def south():Option[Node]
}

case class NorthWestCorner(south:Some[Node], east:Some[Node]) extends Node {
  def west = None
  def north = None
}

case class NorthEdge(south:Some[Node], east:Some[Node], west:Some[Node]) {
  def north = None
}

case class NorthEastCorner(south:Some[Node], west:Some[Node]) {
  def north = None
  def east = None
}

case class SouthEastCorner(north:Some[Node], west: Some[Node]) {
  def east = None
  def south = None
}

case class SouthWestCorner(north:Some[Node], east:Some[Node]) {
  def south = None
  def west = None
}

case class FractalGrid(initial:Coord, iterations:Int, clamp:Int, roughness:Float, seed:Int) {
  val random = new Random(seed)


}