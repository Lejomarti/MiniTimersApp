package com.alejo.minitimers.ui.viewmodels

import android.annotation.SuppressLint
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejo.minitimers.data.ChronometerDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChronometerViewModel(
    private val chronometerDataStore: ChronometerDataStore
) : ViewModel() {

    private val _chronometerWasInitialized = MutableStateFlow(false)
    val chronometerWasInitialized: StateFlow<Boolean> = _chronometerWasInitialized

    private val _chronometerIsRunning = MutableStateFlow(false)
    val chronometerIsRunning: StateFlow<Boolean> = _chronometerIsRunning

    private val _chronometerIsPaused = MutableStateFlow(false)
    val chronometerIsPaused: StateFlow<Boolean> = _chronometerIsPaused

    private val _startTime = MutableStateFlow(0L)
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private var timerJob: Job? = null


    override fun onCleared() {
        super.onCleared()
        Log.d("alejoIsTalking", "ChronometerViewModel cleared")
        timerJob?.cancel()
    }


    fun pauseChronometer() {
        if (_chronometerIsRunning.value) {
            Log.d("alejoIsTalking", "pauseChronometer called")

            _elapsedTime.value = SystemClock.elapsedRealtime() - _startTime.value
            _chronometerIsRunning.value = false
            _chronometerIsPaused.value = true
            timerJob?.cancel()
        }
    }

    fun resumeChronometer() {
        if (_chronometerIsPaused.value) {
            Log.d("alejoIsTalking", "resumeChronometer called")

            _startTime.value = SystemClock.elapsedRealtime() - _elapsedTime.value
            _chronometerIsRunning.value = true
            _chronometerIsPaused.value = false
            startTimerJob()
        }
    }


    private fun startTimerJob() {
        if (timerJob?.isActive == false) { // Solo inicia si no está activo
            timerJob = viewModelScope.launch {
                Log.d("alejoIsTalking", "timerJob started")
                while (_chronometerIsRunning.value) {
                    _elapsedTime.value = (SystemClock.elapsedRealtime() - _startTime.value)
                    _progress.value = (_elapsedTime.value % 60000L) / 60000f
                    delay(10L)
                    Log.d("alejoIsTalking", "timerJob ticking: ${_elapsedTime.value}")
                }
                Log.d("alejoIsTalking", "timerJob stopped")
            }
        } else if (_chronometerIsRunning.value && timerJob == null) {
            timerJob = viewModelScope.launch {
                Log.d("alejoIsTalking", "timerJob started")
                while (_chronometerIsRunning.value) {
                    _elapsedTime.value = (SystemClock.elapsedRealtime() - _startTime.value)
                    _progress.value = (_elapsedTime.value % 60000L) / 60000f
                    delay(10L)
                    Log.d("alejoIsTalking", "timerJob ticking: ${_elapsedTime.value}")
                }
                Log.d("alejoIsTalking", "timerJob stopped")
            }
        }
    }

    init {
        viewModelScope.launch {
            chronometerDataStore.startTime.collect { _startTime.value = it }
            chronometerDataStore.elapsedTime.collect { _elapsedTime.value = it }
            chronometerDataStore.isRunning.collect {
                _chronometerIsRunning.value = it
                _chronometerWasInitialized.value = it || _elapsedTime.value > 0
            }

            // Si estaba corriendo, reinicia el cronómetro
            if (_chronometerIsRunning.value) {
                startTimerJob()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun startChronometer() {
        viewModelScope.launch {
            val now = SystemClock.elapsedRealtime() - _elapsedTime.value
            _startTime.value = now
            _chronometerWasInitialized.value = true
            _chronometerIsRunning.value = true
            _chronometerIsPaused.value = false


                chronometerDataStore.saveStartTime(_startTime.value)
                chronometerDataStore.saveIsRunning(true)

            startTimerJob()
        }
    }

    fun cancelChronometer() {
        _chronometerWasInitialized.value = false
        _chronometerIsRunning.value = false
        _chronometerIsPaused.value = false
        _elapsedTime.value = 0L
        _progress.value = 0f

        timerJob?.cancel()

        viewModelScope.launch {
            chronometerDataStore.clear() // ← si no lo tienes, agrégalo con inyección o parámetro
        }
    }
}