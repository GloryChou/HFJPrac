package per.zyf.hfjp.midi.ultimate

import com.sun.xml.internal.fastinfoset.util.StringArray
import java.awt.BorderLayout
import java.awt.Component
import java.awt.GridLayout
import java.awt.Label
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.sound.midi.*
import javax.swing.*

/**
 * Beat Box
 * @author glorychou
 * @create 2017/8/19 22:55
 */
class BeatBox {
    var mainPanel: JPanel? = null
    var checkboxList: ArrayList<JCheckBox> = ArrayList<JCheckBox>()

    val sequencer: Sequencer = MidiSystem.getSequencer()
    var sequence: Sequence? = null
    var track: Track? = null
    var theFrame: JFrame = JFrame("Cyber BeatBox")

    // list all of instrument names
    val instrumentNames: Array<String> = arrayOf("Bass Drum", "Closed Hi-Hat",
            "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal", "Hand Clap",
            "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
            "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo",
            "Open Hi Conga"
    )
    // the real instrument code
    val instruments: IntArray = intArrayOf(35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63)

    // main function of generating GUI
    fun buildGUI() {
        theFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        var layout = BorderLayout()
        var background = JPanel()
        background.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        var buttonBox = Box(BoxLayout.Y_AXIS)

        var start = JButton("Start")
        start.addActionListener(MyStartListener())
        buttonBox.add(start)

        var stop = JButton("Stop")
        stop.addActionListener(MyStopListener())
        buttonBox.add(stop)

        var upTempo = JButton("Tempo Up")
        upTempo.addActionListener(MyUpTempoListener())
        buttonBox.add(upTempo)

        var downTempo = JButton("Tempo Down")
        downTempo.addActionListener(MyDownTempoListener())
        buttonBox.add(downTempo)

        var nameBox = Box(BoxLayout.Y_AXIS)
        for(i in 0..15) {
            nameBox.add(Label(instrumentNames[i]))
        }

        background.add(BorderLayout.EAST, buttonBox)
        background.add(BorderLayout.WEST, nameBox)

        theFrame.contentPane.add(background)

        var grid: GridLayout = GridLayout(16, 16)
        grid.vgap = 1
        grid.hgap = 2
        mainPanel = JPanel(grid)
        background.add(BorderLayout.CENTER, mainPanel)

        for(i in 0..255) {
            var c: JCheckBox = JCheckBox()
            c.isSelected = false
            checkboxList.add(c)
            mainPanel?.add(c)
        }

        setUpMidi()

        theFrame.setBounds(50, 50, 300, 300)
        theFrame.pack()
        theFrame.isVisible = true
    }

    fun setUpMidi() = try {
        sequencer.open()
        sequence = Sequence(Sequence.PPQ, 4)
        track = sequence?.createTrack()
        sequencer.tempoInBPM = 120f
    }catch(e: Exception) {
        e.printStackTrace()
    }

    fun buildTrackAndStart() {
        var trackList: IntArray? = null

        sequence?.deleteTrack(track)
        track = sequence?.createTrack()

        for(i in 0..15) {
            trackList = IntArray(16)

            var key: Int = instruments[i]

            for(j in 0.. 15) {
                var jc: JCheckBox = checkboxList.get(j + 16 * i)
                if(jc.isSelected) {
                    trackList[j] = key
                } else {
                    trackList[j] = 0
                }
            }

            makeTracks(trackList)
            track?.add(makeEvent(176, 1, 127, 0, 16))
        }

        track?.add(makeEvent(192, 9, 1, 0, 15))
        try {
            sequencer.sequence = sequence
            sequencer.loopCount = Sequencer.LOOP_CONTINUOUSLY
            sequencer.start()
            sequencer.tempoInBPM = 120f
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class MyStartListener : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            buildTrackAndStart()
        }
    }

    inner class MyStopListener : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            sequencer.stop()
        }
    }

    inner class MyUpTempoListener : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            val tempoFactor : Float = sequencer.tempoFactor
            sequencer.tempoFactor = tempoFactor * 1.03f
        }
    }

    inner class MyDownTempoListener : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            val tempoFactor : Float = sequencer.tempoFactor
            sequencer.tempoFactor = tempoFactor * .97f
        }
    }

    fun makeTracks(list: IntArray) {
        for (i in 0..15) {
            var key = list[i]
            if (key != 0) {
                track?.add(makeEvent(144, 9, key, 100, i.toLong()))
                track?.add(makeEvent(128, 9, key, 100, (i + 1).toLong()))
            }
        }
    }

    fun makeEvent(comd: Int, chan: Int, one: Int, two: Int, tick: Long): MidiEvent? {
        var event: MidiEvent? = null
        try {
            var a: ShortMessage = ShortMessage()
            a.setMessage(comd, chan, one, two)
            event = MidiEvent(a, tick)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return event
    }
}

fun main(args: Array<String>) {
    BeatBox().buildGUI()
}