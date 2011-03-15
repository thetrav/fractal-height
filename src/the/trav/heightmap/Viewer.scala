package the.trav.heightmap

import javax.swing.{JPanel, JFrame}
import java.awt.{Color, Graphics, Graphics2D}

object Viewer {

  val width = 800
  val height = 600


  val heightMapIterations = 1
  val heightMapScale = 20.0
  val heightMapSquareSize = 3 * heightMapScale
  val heightMapClamp = 400.00

  def drawHeightMap(map:Map[Coord, Double], g:Graphics2D) {
    val maxHeight = map.values.reduceLeft((a:Double, b:Double) => Math.max(a, b))
    g.scale(width / heightMapSquareSize, height / heightMapSquareSize)
    for(c <- map.keySet) {
      val height = map(c)
      val normalised = 255/maxHeight * height
      val positive = if (normalised < 0) 0 else normalised.asInstanceOf[Int]
      g.setColor(new Color(positive, positive, positive))
      val oval = new java.awt.geom.Ellipse2D.Double(c.x, c.y, 10.0, 10.0)
      g.fill(oval)
    }
  }

  def main(args:Array[String]) {
    val heightMap = MappedProgressive.produceHeightMap(heightMapIterations, heightMapScale, heightMapClamp)

    val frame = new JFrame("heightmap viewer")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(width, height)

    frame.getContentPane().add(new JPanel() {
      override def paintComponent(g:Graphics) {
        g.setColor(Color.blue)
        g.fillRect(0,0,width, height)
        val g2 = g.asInstanceOf[Graphics2D]
        val originalTransform = g2.getTransform
        drawHeightMap(heightMap, g2)
        g2.setTransform(originalTransform)
      }
    })

    frame.setVisible(true)

  }
}