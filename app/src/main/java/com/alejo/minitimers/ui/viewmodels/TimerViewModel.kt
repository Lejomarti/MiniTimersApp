package com.alejo.minitimers.ui.viewmodels

import android.os.CountDownTimer
import android.os.SystemClock
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejo.minitimers.data.TimersDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TimerViewModel(private val timersDataStore: TimersDataStore) : ViewModel() {
    //TODO: Hacer el viewmodel para el timer y despejar el MiniTimerScreen de tanta logica

    private val _timers = MutableStateFlow<List<Pair<String, Long>>>(emptyList())
    val timers: StateFlow<List<Pair<String, Long>>> = _timers

    private val _wasInitialized = mutableStateOf(false)
    private val _isRunning = mutableStateOf(false)
    private val _isPaused = mutableStateOf(false)
    val wasInitialized: State<Boolean> = _wasInitialized
    val isRunning: State<Boolean> = _isRunning
    val isPaused: State<Boolean> = _isPaused

    private val _startTime = mutableLongStateOf(0L)
    private val _pausedTime = mutableLongStateOf(0L)
    private val _elapsedTime = mutableLongStateOf(0L)
    val elapsedTime: State<Long> = _elapsedTime

    private val _upperList = mutableStateListOf<Pair<String, Long>>()
    private val _lowerList = mutableStateListOf<Long>()
    private val _currentTimer = mutableStateOf<Long?>(null)

    val upperList: List<Pair<String, Long>> = _upperList
    val lowerList: List<Long> = _lowerList
    val currentTimer: State<Long?> = _currentTimer

    private val _timeRemaining = mutableLongStateOf(0L)
    val timeRemaining: State<Long> = _timeRemaining

    private var countDownTimer: CountDownTimer? = null
    private var chronoJob: Job? = null
    private val intervals: Long = 10

    init {
        loadTimers()
    }

    private fun loadTimers() {
        viewModelScope.launch {
            timersDataStore.timersFlow.collectLatest { fetchedTimers ->
                _timers.value = fetchedTimers
                resetLists(fetchedTimers)

                if(fetchedTimers.isNotEmpty() && !_wasInitialized.value){
                    _wasInitialized.value = false
                }
            }
        }
    }

    // Función para iniciar el cronómetro
    private fun startChrono() {
        chronoJob?.cancel() // Cancelar cualquier trabajo previo
        chronoJob = viewModelScope.launch {
            if (_isPaused.value) {
                _startTime.longValue = SystemClock.elapsedRealtime() - _pausedTime.longValue
            } else {
                _startTime.longValue = SystemClock.elapsedRealtime()
            }
            _isPaused.value = false
            _isRunning.value = true
            while (_isRunning.value) {
                _elapsedTime.longValue = SystemClock.elapsedRealtime() - _startTime.longValue
                delay(100) // Actualización frecuente
            }
        }
    }

    // Pausar el cronómetro
    private fun pauseChrono() {
        chronoJob?.cancel()
        _isPaused.value = true
        _pausedTime.longValue = SystemClock.elapsedRealtime() - _startTime.longValue
        _isRunning.value = false
    }

    // Reanudar el cronómetro
    private fun resumeChrono() {
        if (_isPaused.value) {
            startChrono()
        }
    }

    // Cancelar el cronómetro
    private fun cancelChrono() {
        chronoJob?.cancel()
        _elapsedTime.longValue = 0L
        _startTime.longValue = 0L
        _pausedTime.longValue = 0L
        _isRunning.value = false
    }

    // Resetear las listas
    private fun resetLists(initialTimers: List<Pair<String, Long>>) {
        _upperList.clear()
        _upperList.addAll(initialTimers)
        _lowerList.clear()
        _wasInitialized.value = false
    }

    // Iniciar el temporizador
    fun startTimer(onTimerFinished: () -> Unit) {
        if (_upperList.isNotEmpty()) {
            if (!_wasInitialized.value) {
                _wasInitialized.value = true
                startChrono() // Solo inicia el cronómetro una vez al principio
            }
            _currentTimer.value = _upperList.removeAt(0).second
            _timeRemaining.longValue = _currentTimer.value ?: 0L

            countDownTimer?.cancel() // Cancela cualquier temporizador previo
            countDownTimer = object : CountDownTimer(_timeRemaining.longValue, intervals) {
                override fun onTick(millisUntilFinished: Long) {
                    _timeRemaining.longValue = millisUntilFinished
                }

                override fun onFinish() {
                    _currentTimer.value?.let {
                        _lowerList.add(it)
                        _currentTimer.value = null
                        onTimerFinished() // Llama a la función para manejar el sonido y el siguiente timer
                    }

                    if (_upperList.isNotEmpty()) {
                        startTimer(onTimerFinished) // Continúa con el siguiente temporizador sin tocar el cronómetro
                    } else {
                        _isRunning.value = false
                        pauseChrono() // Solo se pausa cuando todos los timers terminan
                    }
                }
            }.start()
        } else {
            _isRunning.value = false
            _wasInitialized.value = false
            pauseChrono() // Se detiene el cronómetro solo si ya no hay timers
        }
    }

    fun onTimerFinish(playSound: () -> Unit) {
        _currentTimer.value?.let {
            _lowerList.add(it)
            _currentTimer.value = null
            playSound()
        }

        if (_upperList.isNotEmpty()) {
            startTimer(playSound) // Continúa con el siguiente temporizador sin tocar el cronómetro
        } else {
            _isRunning.value = false
            pauseChrono()
        }
    }

    // Pausar el temporizador
    fun pauseTimer() {
        countDownTimer?.cancel()
        _isRunning.value = false
        _isPaused.value = true
        pauseChrono()
    }

    // Reanudar el temporizador
    fun resumeTimer(onTimerFinished: () -> Unit) {
        resumeChrono()
        _isRunning.value = true
        _isPaused.value = false

        countDownTimer = object : CountDownTimer(_timeRemaining.longValue, intervals) {
            override fun onTick(millisUntilFinished: Long) {
                _timeRemaining.longValue = millisUntilFinished
            }

            override fun onFinish() {
                onTimerFinish(onTimerFinished)
            }
        }.start()
    }

    // Cancelar el temporizador
    fun cancelTimer() {
        pauseTimer()
        _wasInitialized.value = false
        _isRunning.value = false
        _isPaused.value = false
        _timeRemaining.longValue = 0L
        _currentTimer.value = null
        resetLists(_timers.value)
        cancelChrono()
    }

    fun removeAllTimers() {
        viewModelScope.launch {
            timersDataStore.removeAllTimers()
        }
    }
}