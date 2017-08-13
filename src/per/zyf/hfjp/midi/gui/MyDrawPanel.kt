package per.zyf.hfjp.midi.gui

import java.awt.*
import javax.sound.midi.*
import javax.swing.*

/**
 * Operating canvas
 * @author glorychou
 * @create 2017/8/13 11:44
 */
class MyDrawPanel: JPanel(), ControllerEventListener {
    var msg: Boolean = false

    override fun controlChange(event: ShortMessage?) {
        msg = true
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        if(msg) {
            val g2: Graphics2D = g as Graphics2D

            val r: Int = (Math.random() * 250).toInt()
            val gr: Int = (Math.random() * 250).toInt()
            val b: Int = (Math.random() * 250).toInt()

            g2.color = Color(r, gr, b)

            val ht: Int = ((Math.random() * 120) + 10).toInt()
            val width: Int = ((Math.random() * 120) + 10).toInt()

            val x: Int = ((Math.random() * 40) + 10).toInt()
            val y: Int = ((Math.random() * 40) + 10).toInt()

            g.fillRect(x, y, ht, width)
            msg = false
        }
    }
}