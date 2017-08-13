package per.zyf.hfjp.midi.cmd

import javax.sound.midi.*

/**
 * MiniMusic
 * @author glorychou
 * @create 2017/8/6 11:31
 */
class MiniMusicCmdLine {
    fun play(instrument: Int, note: Int) = try{
        /**
         * Step:
         * Sequencer --> Sequence --> Track --> ShortMessage --> MidiEvent
         *
         * Desc:
         * Sequencer: like a CD player
         * Sequence: like a single CD
         * Track: like a song in the single CD
         * ShortMessage and MidiEvent: music scores consist of a series of ShortMessage and MidiEvent
         *                             the ShortMessage describes what to do
         *                             the MidiEvent describes when to do
         */
        val player: Sequencer = MidiSystem.getSequencer()
        player.open()
        val seq: Sequence = Sequence(Sequence.PPQ, 4)
        val track: Track = seq.createTrack()

        val first: ShortMessage = ShortMessage()
        // 192: Change Instrument
        first.setMessage(192, 1, instrument, 0)
        val changeInstrument: MidiEvent = MidiEvent(first, 1)
        track.add(changeInstrument)

        val a: ShortMessage = ShortMessage()
        // 144: Start
        a.setMessage(144, 1, note, 100)
        val noteOn: MidiEvent = MidiEvent(a, 1)
        track.add(noteOn)

        val b: ShortMessage = ShortMessage()
        // 128: Stop
        b.setMessage(128, 1, note, 100)
        val noteOff: MidiEvent = MidiEvent(b, 16)
        track.add(noteOff)
        player.setSequence(seq)
        player.start()
    }catch (e: Exception) {
        e.printStackTrace()
    }
}

fun main(args: Array<String>) {
    val mini: MiniMusicCmdLine = MiniMusicCmdLine()
    if(args.size < 2)
        print("Don't forget the instrument and note args!")
    else {
        val instrument: Int = args[0].toInt()
        val note: Int = args[1].toInt()
        mini.play(instrument, note)
    }
}