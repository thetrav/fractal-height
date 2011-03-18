package the.trav.heightmap

import com.sun.j3d.utils.universe.SimpleUniverse
import javax.media.j3d._
import javax.vecmath.{Vector3d, Color3f, Point3d}

object Viewer3D {
  val width  = 800
  val height = 600

  def main(args:Array[String]) {

    val heightMapIterations = 3
    val pointsPerRow = 9//(Math.pow(2, heightMapIterations)-1).asInstanceOf[Int]

    val heightMapScale = 1.0
    val heightMapSquareSize = 2.0
    val heightMapClamp = 4.00

    val heightMap = MappedProgressive.produceHeightMap(heightMapIterations, heightMapScale, heightMapClamp)
//    val h = Map(
//      Coord(0.0,1.5) -> 2.745923930411275,
//      Coord(0.125,0.75) -> 1.6454344008726904,
//      Coord(0.25,0.625) -> 1.279549849551992,
//      Coord(0.375,1.25) -> 1.5691486910591042,
//      Coord(0.5,0.5) -> 1.201325335546904,
//      Coord(0.625,2.0) -> 1.4416570832557596,
//      Coord(0.75,0.25) -> 1.5379138073546974,
//      Coord(0.875,0.0) -> 1.5465942337044467,
//      Coord(1.0,1.75) -> 1.5456662268141663,


    val maxHeight = heightMap.values.reduceLeft((a:Double, b:Double) => Math.max(a, b))
    val scale = heightMapSquareSize / (pointsPerRow-1)
    def p(x:Double, y:Double) = {
      val c = Coord(x*scale, y*scale)
      val height = heightMap(c)
      new Point3d(c.x, c.y, height)
    }
    val quads = for(x <- (0 to pointsPerRow-1).sliding(2); y <- (0 to pointsPerRow-1).sliding(2)) yield {
      List(p(x(0), y(0)),
           p(x(1), y(0)),
           p(x(1), y(1)),
           p(x(0), y(1)))
    }
    val points = quads.flatMap((x:List[Point3d]) => x.iterator).toList
//    val points = List(new Point3d(0.0, 0.0, 3.3799054359392238), new Point3d(0.5, 0.0, 2.2192600038711947), new Point3d(0.5, 0.5, 1.201325335546904), new Point3d(0.0, 0.5, 2.686367975174074))
//    val points = List(new Point3d(0.0, 0.0, 0.3799054359392238), new Point3d(0.5, 0.0, 0.2192600038711947), new Point3d(0.5, 0.5, 0.201325335546904), new Point3d(0.0, 0.5, 0.686367975174074))
//    println("points="+points)

    val universe = new SimpleUniverse()
    val group = new BranchGroup()

    val mesh = buildMesh(points, maxHeight)

    val transformGroup = new TransformGroup()
    val transform = new Transform3D()
    val x = -1
    val y = -1
    val z = -3
    transform.setTranslation(new Vector3d(x,y,z))
    
//    transform.setScale(0.01)

    transformGroup.setTransform(transform)
    transformGroup.addChild(new Shape3D(mesh))

    group.addChild(transformGroup)
//    group.addChild(new Shape3D(mesh))
    universe.addBranchGraph(group)

    universe.getViewingPlatform().setNominalViewingTransform()


  }

  def buildMesh(points: List[Point3d], maxHeight:Double) = {
    val flags = GeometryArray.COORDINATES | GeometryArray.COLOR_3
    println("vertexes:"+points.size)
    val mesh = new QuadArray(points.size, flags)
    var index = 0
    points.foreach((p:Point3d) => {
      mesh.setCoordinate(index, p)
      val color = (p.z/maxHeight).asInstanceOf[Float]
//      println("height"+p.z + "max:"+maxHeight + "Color: "+color)
      mesh.setColor(index, new Color3f(color, color, color))
      index += 1
    })
    mesh
  }
}