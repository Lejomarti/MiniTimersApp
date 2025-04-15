package com.alejo.minitimers.data

import com.alejo.minitimers.R

data class SoundOption(
    val id:String,
    val name: String,
    val resId: Int
)

object SoundList {
    val sounds = listOf(
        SoundOption("arcade", "Arcade", R.raw.sound_arcade1),
        SoundOption("bark", "Bark", R.raw.sound_bark1),
        SoundOption("basketball", "Basketball Buzzer", R.raw.sound_basketball_buzzer),
        SoundOption("bell1", "Bell 1", R.raw.sound_bell1),
        SoundOption("bell2", "Bell 2", R.raw.sound_bell2),
        SoundOption("cellphone", "Cellphone Bell", R.raw.sound_cellphone_bell),
        SoundOption("cuckoo", "Cuckoo", R.raw.sound_cuckoo),
        SoundOption("electric_alarm", "Electric Alarm", R.raw.sound_electric_alarm),
        SoundOption("fight_bells", "Fight Bells", R.raw.sound_fight_bells),
        SoundOption("hockey_buzzer", "Hockey Buzzer", R.raw.sound_ice_hockeybuzzer1),
        SoundOption("trombone", "Trombone", R.raw.sound_trombone1),
        SoundOption("wrong_buzzer", "Wrong Buzzer", R.raw.sound_wrong_buzzer1),
        SoundOption("timer", "Default Timer", R.raw.timer_sound)
    )

}