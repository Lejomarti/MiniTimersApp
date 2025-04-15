package com.alejo.minitimers.utils.Sounds

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool



class SoundManager {
    private var soundPool: SoundPool? = null

    fun playSound(context: Context, resId: Int) {
        soundPool?.release()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()

        val sp = soundPool ?: return

        val soundId = sp.load(context, resId, 1)

        sp.setOnLoadCompleteListener { _, loadedSoundId, status ->
            if (status == 0) {
                sp.play(loadedSoundId, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
