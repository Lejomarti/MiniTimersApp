package com.alejo.minitimers.utils.Sounds

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.alejo.minitimers.R

class SoundManager(context: Context) {
    private val soundPool: SoundPool
    private var soundId: Int = 0

    init {
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()

        soundId = soundPool.load(context, R.raw.timer_sound, 1)
    }


    fun playSound() {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }
}
