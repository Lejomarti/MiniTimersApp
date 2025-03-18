package com.alejo.minitimers.utils

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

        // Cargar el sonido desde res/raw
        soundId = soundPool.load(context, R.raw.timer_sound, 1)
    }

    // Función para reproducir el sonido
    fun playSound() {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    // Liberar recursos cuando ya no se usa
    fun release() {
        soundPool.release()
    }
}
