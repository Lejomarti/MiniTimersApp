package com.alejo.minitimers.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChronometerViewModel(application: Application):AndroidViewModel (application){
    val chronometerWasInitialized = MutableStateFlow(false)
    val chronometerIsRunning = MutableStateFlow(false)
    val chronometerStartTime = MutableStateFlow(0L)
    val chronometerElapsedTime = MutableStateFlow(0L)
    var chronometerDisplayTime = MutableStateFlow("00:00:0")
    val progressInMinute = MutableStateFlow(0f)
    private var timerJob: Job? = null

    private fun startTimerLoop() {
        timerJob = viewModelScope.launch {
            while (chronometerIsRunning.value) {
                val now = SystemClock.elapsedRealtime()
                val elapsed = now - chronometerStartTime.value

                chronometerElapsedTime.value = elapsed

                chronometerDisplayTime.value = formatElapsedTime(elapsed)

                progressInMinute.value = (elapsed % 60000).toFloat() / 60000f

                delay(100)
            }
        }
    }

    fun startChronometer() {
        chronometerWasInitialized.value = true
        chronometerIsRunning.value = true
        chronometerStartTime.value = SystemClock.elapsedRealtime()
        chronometerElapsedTime.value = 0L
        startTimerLoop()
    }


    fun pauseChronometer() {
        chronometerIsRunning.value = false
        timerJob?.cancel()
    }

    fun resumeChronometer() {
        if (!chronometerIsRunning.value) {
            chronometerIsRunning.value = true
            chronometerStartTime.value = SystemClock.elapsedRealtime() - chronometerElapsedTime.value
            startTimerLoop()
        }
    }

    fun cancelChronometer() {
        chronometerWasInitialized.value = false
        chronometerIsRunning.value = false
        chronometerElapsedTime.value = 0L
        chronometerStartTime.value = 0L
        chronometerDisplayTime.value = "00:00:0"
        progressInMinute.value = 0f
        timerJob?.cancel()
    }

    private fun formatElapsedTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val tenths = (milliseconds % 1000) / 100

        return String.format("%02d:%02d:%01d", minutes, seconds, tenths)
    }











}
