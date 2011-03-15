package the.trav.heightmap

object Temp {
  def main(args:Array[String]) {
    for(x <- (0 to 10).sliding(2); y <- (0 to 5).sliding(1)) yield { (x, y) }
  }
}