package the.trav.heightmap

import java.util.Random

object MappedProgressive {
  def main(args:Array[String]) { println(produceHeightMap(1, 20.0, 400.0)) }

  val rand = new Random("The Trav".hashCode())
  def random(clamp:Double) = rand.nextDouble() * clamp

  def produceHeightMap(iterations:Int, topScale:Double, clamp:Double) = {
    def r = random(clamp)
    def point(x:Double, y:Double) = Coord(x, y) -> r
    val topMap = Map(
      point(0.0, 0.0),        point(topScale, 0.0),        point(topScale*2, 0.0),
      point(0.0, topScale),   point(topScale, topScale),   point(topScale*2, topScale),
      point(0.0, topScale*2), point(topScale, topScale*2), point(topScale*2, topScale*2)
    )
    fillDown(topMap, iterations-1, topScale, clamp, 3)                           
  }

  def squareNeighbors(c:Coord, s:(Coord,Direction) => Coord, max:Double) = {
    def step(d:Direction) = s(c, d)
    val result = c match {
      case Coord(0, _)             => Set(step(North), step(East),  step(South))
      case Coord(x, _) if x >= max => Set(step(North), step(South), step(West))
      case Coord(_, 0)             => Set(step(West),  step(South), step(East))
      case Coord(_, y) if y >= max => Set(step(West),  step(North), step(East))
      case _                       => Set(step(North), step(South), step(East),  step(West))
    }

    result
  }

  def neighborAverage(coord:Coord, neighbors:Coord => Set[Coord], point:Coord => Double) = {
    val points = for(c <- neighbors(coord)) yield point(c)
    points.sum / points.count((Double) => true)
  }

  def fillDown(map:Map[Coord, Double], iterations:Int, scale:Double, clamp:Double, squareSize:Int) = {
    //diamonds
    val withDiamonds = map ++ diamonds(map, iterations, scale, clamp, squareSize)
    //squares
    def step(c:Coord, d:Direction) = c.step(d, scale/2)
    val max = (squareSize-1)*scale
    def avg(c:Coord) = neighborAverage(c, squareNeighbors(_, step, max), withDiamonds(_))

    val horizontals = for(x <- (0 to squareSize-1).sliding(2);
                          y <- (0 to squareSize-1).sliding(1)) yield {
      val midPoint = Coord(x(0)*scale + scale/2, y(0)*scale)
      midPoint -> avg(midPoint)
    }

    val verticals = for(y <- (0 to squareSize-1).sliding(2);
                        x <- (0 to squareSize-1).sliding(1)) yield {
      val midPoint = Coord(x(0)*scale, y(0)*scale + scale/2)
      midPoint -> avg(midPoint)
    }
    withDiamonds ++ verticals ++ horizontals
  }

  def diamonds(map:Map[Coord, Double], iterations:Int, scale:Double, clamp:Double, squareSize:Int) = {
    def point(x:Double, y:Double) = map(Coord(x*scale, y*scale))
    for(x <- (0 to squareSize-1).sliding(2);
                       y <- (0 to squareSize-1).sliding(2)) yield {
          val midPoint = Coord(x(0)*scale + scale/2, y(0)*scale + scale/2)
          val heightSum = point(x(0), y(0)) + point(x(1), y(0))
                        + point(x(0), y(1)) + point(x(1), y(1))
          val heightAverage = heightSum / 4.0
          val r = random(clamp) - clamp/2.0
          midPoint -> (heightAverage + r)
    }
  }
}
