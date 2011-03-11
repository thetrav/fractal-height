package the.trav

import java.util.Random
import javax.swing._
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Color, BorderLayout, Graphics2D, Graphics}

case class Coord(x:Int, y:Int)
case class Range(min:Int, max:Int)

object Landscape {

  var doPaint = (g:Graphics2D) => {}

  def main(args: Array[String]) {
    val frame = new JFrame("fractal line")
    frame.setSize(800,600)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)



    val layout = new JPanel(new BorderLayout())
    frame.getContentPane().add(layout)

    val paintPanel = new JPanel(){
       override def paint(g:Graphics) {
         doPaint(g.asInstanceOf[Graphics2D])
       }
    }
    layout.add(paintPanel, BorderLayout.CENTER)

    val controls = new JPanel()
    layout.add(controls, BorderLayout.SOUTH)

    def addField(label:String, value:String, container:JPanel) = {
      container.add(new JLabel(label))
      val field = new JTextField(value)
      container.add(field)
      field
    }

    val control = addField(_:String, _:String, controls)

    val clampField = control("clamp", "400")
    val iterationField = control("iterations", "5")
    val roughnessField = control("roughness", "0.5")
    val scaleField = control("scale", "1")

    def asInt(field:JTextField) = {
      BigDecimal(field.getText()).toInt
    }

    def asFloat(field:JTextField) = {
      BigDecimal(field.getText()).toFloat
    }

    def updateLine = () => {
      val line = FractalLine(asInt(iterationField),
        asFloat(roughnessField),
        asInt(clampField),
        asInt(scaleField))
      doPaint = drawLine(_, line.list)
      paintPanel.invalidate()
      paintPanel.repaint()
    }

    val generateButton = new JButton("generate")
    controls.add(generateButton)
    generateButton.addActionListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent) {
        updateLine()
      }
    })

    frame.setVisible(true)
  }

  def drawLine(g:Graphics2D, points:List[Int]) {
    val originalTransform = g.getTransform()
    g.setColor(Color.white)
    g.fillRect(0,0,800,600)
    g.setColor(Color.red)
    val xInterval = 600/points.length
    points.sliding(2).foreach((window:List[Int]) => {
      g.drawLine(0, window(0), xInterval, window(1))
      g.translate(xInterval, 0)
    })

    g.setTransform(originalTransform)
  }
}

case class FractalLine(iterations:Int, roughness:Float, clamp:Int, scale:Int) {
  val random = new Random("Travis Dixon".hashCode()+scale)
  val startList = List(random.nextInt(clamp),
                         random.nextInt(clamp))
//  println("iterations="+iterations)
  private def splitList(list:List[Int], splitClamp:Int) = {
    def displace(a:Int, b:Int) = {
      val avg = (a + b) / 2
      val offset = random.nextInt(splitClamp*2) - splitClamp
      avg + offset
    }

    list.head ::
            list.sliding(2).toList.flatMap( (window:List[Int]) => {
                val newMidPoint = displace(window(0), window(1))
                List(newMidPoint, window(1))
            }
  )}

  private def splitIteration(l:List[Int], it:Int) = {
    val reduction = (1.0/it)*roughness
    val smallClamp = clamp * reduction
    val splitClamp = if(smallClamp < 1.0) 1 else smallClamp.asInstanceOf[Int]
    splitList(l, splitClamp)
  }

  def list = (1 to iterations).foldLeft[List[Int]](startList)(splitIteration).toList

}