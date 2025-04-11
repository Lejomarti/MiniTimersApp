package com.alejo.minitimers.ui.viewmodels

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alejo.minitimers.data.ChronometerDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ChronometerViewModel(application: Application):AndroidViewModel (application){
    private val context = application.applicationContext

    val chronometerWasInitialized = MutableStateFlow(false)
    val chronometerIsRunning = MutableStateFlow(false)
    val chronometerStartTime = MutableStateFlow(0L)
    val chronometerElapsedTime = MutableStateFlow(0L)
    var chronometerDisplayTime = MutableStateFlow("00:00:0")
    val progressInMinute = MutableStateFlow(0f)
    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            val wasInitialized = ChronometerDataStore.readWasInitialized(getApplication()).first()
            val isRunning = ChronometerDataStore.readIsRunning(getApplication()).first()
            val startTime = ChronometerDataStore.readStartTime(getApplication()).first()
            val elapsedTime = ChronometerDataStore.readElapsedTime(getApplication()).first()

            chronometerWasInitialized.value = wasInitialized
            chronometerIsRunning.value = isRunning
            chronometerStartTime.value = startTime
            chronometerElapsedTime.value = elapsedTime
        }
    }


    private fun startTimerLoop() {
        timerJob = viewModelScope.launch {
            while (isActive && chronometerIsRunning.value) {
                val now = SystemClock.elapsedRealtime()
                val elapsed = now - chronometerStartTime.value + chronometerElapsedTime.value

                chronometerDisplayTime.value = formatElapsedTime(elapsed)
                progressInMinute.value = (elapsed % 60000).toFloat() / 60000f
                delay(100)
            }
        }
    }

    fun startChronometer() {
        val startTime = SystemClock.elapsedRealtime()
        chronometerStartTime.value = startTime
        chronometerElapsedTime.value = 0L
        chronometerWasInitialized.value = true
        chronometerIsRunning.value = true

        timerJob?.cancel()
        startTimerLoop()

        viewModelScope.launch {
            ChronometerDataStore.saveStartTime(getApplication(), startTime)
            ChronometerDataStore.saveElapsedTime(getApplication(), 0L)
            ChronometerDataStore.saveWasInitialized(getApplication(), true)
            ChronometerDataStore.saveIsRunning(getApplication(), true)
        }
    }


    fun pauseChronometer() {
        viewModelScope.launch {
            timerJob?.cancel()
            timerJob = null

            val currentElapsed = SystemClock.elapsedRealtime() - chronometerStartTime.value
            chronometerElapsedTime.value += currentElapsed
            chronometerIsRunning.value = false

            ChronometerDataStore.saveElapsedTime(context, chronometerElapsedTime.value)
            ChronometerDataStore.saveIsRunning(context, false)
        }
    }

    fun resumeChronometer() {
        viewModelScope.launch {
            val startTime = SystemClock.elapsedRealtime()
            chronometerStartTime.value = startTime
            chronometerIsRunning.value = true

            ChronometerDataStore.saveStartTime(context, startTime)
            ChronometerDataStore.saveIsRunning(context, true)

            timerJob?.cancel()
            timerJob = null
            startTimerLoop()
        }
    }

    fun cancelChronometer() {
        viewModelScope.launch {
            timerJob?.cancel()
            timerJob = null

            chronometerWasInitialized.value = false
            chronometerIsRunning.value = false
            chronometerStartTime.value = 0L
            chronometerElapsedTime.value = 0L
            chronometerDisplayTime.value = "00:00:0"
            progressInMinute.value = 0f

            ChronometerDataStore.saveWasInitialized(getApplication(), false)
            ChronometerDataStore.saveIsRunning(getApplication(), false)
            ChronometerDataStore.saveStartTime(getApplication(), 0L)
            ChronometerDataStore.saveElapsedTime(getApplication(), 0L)
        }
    }

    private fun formatElapsedTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val tenths = (milliseconds % 1000) / 100

        return String.format("%02d:%02d:%01d", minutes, seconds, tenths)
    }

}
