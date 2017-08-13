package per.zyf.hfjp.midi.gui

import javax.sound.midi.*
import javax.swing.*

/**
 * A simple music player
 * @author glorychou
 * @create 2017/8/13 11:01
 */
class MiniMusicPlay {
    companion object {
        val f: JFrame = JFrame("My First Music Video")
        var ml: MyDrawPanel? = null
    }

    fun setUpGui() {
        ml = MyDrawPanel()
        f.contentPane = ml
        f.setBounds(30, 30, 300, 300)
        f.isVisible = true
    }

    fun go() {
        setUpGui()

        try{
            val sequencer: Sequencer = MidiSystem.getSequencer()
            sequencer.open()
            sequencer.addControllerEventListener(ml, arrayOf(127).toIntArray())

            val seq: Sequence = Sequence(Sequence.PPQ, 4)
            val track: Track = seq.createTrack()

            for(i in 0..60 step 4) {
                val r = ((Math.random() * 50) + 1).toInt()
                track.add(makeEvent(144, 1, r, 100,  i))
                track.add(makeEvent(176, 1, 127, 0,  i))
                track.add(makeEvent(128, 1, r, 100,  i + 2))
            }

            sequencer.sequence = seq
            sequencer.tempoInBPM = 120F
            sequencer.start()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun makeEvent(comd: Int, chan: Int, one: Int, two: Int, tick: Int): MidiEvent? {
        var event: MidiEvent? = null
        try{
            val a: ShortMessage = ShortMessage()
            a.setMessage(comd, chan, one, two)
            event = MidiEvent(a, tick.toLong())
        }catch (e: Exception) {

        }
        return event
    }
}

fun main(args: Array<String>) {
    val mini: MiniMusicPlay = MiniMusicPlay()
    mini.go()
}