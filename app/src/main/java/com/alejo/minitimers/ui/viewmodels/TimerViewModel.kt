package com.alejo.minitimers.ui.viewmodels

import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejo.minitimers.data.TimersDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TimerViewModel(private val timersDataStore: TimersDataStore) : ViewModel() {
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
    private val _lowerList = mutableStateListOf<Pair<String, Long>>()
    private val _currentTimer = mutableStateOf<Long?>(null)

    val upperList: List<Pair<String, Long>> = _upperList
    val lowerList: List<Pair<String,Long>> = _lowerList
    val currentTimer: State<Long?> = _currentTimer

    private val _timeRemaining = mutableLongStateOf(0L)
    val timeRemaining: State<Long> = _timeRemaining

    private var countDownTimer: CountDownTimer? = null
    private var chronoJob: Job? = null
    private val intervals: Long = 10

    private val _repeatCount = mutableIntStateOf(0)
    val repeatCount: State<Int> = _repeatCount
    private var currentRepeatLeft = 0

    private var initialTimers: List<Pair<String, Long>> = emptyList() // Guardar la lista original

    init {
        loadTimers()
    }

    private fun loadTimers() {
        viewModelScope.launch {
            timersDataStore.timersFlow.collectLatest { fetchedTimers ->
                _timers.value = fetchedTimers
                initialTimers = fetchedTimers // Guardar la lista original
                resetLists(fetchedTimers)

                if (fetchedTimers.isNotEmpty() && !_wasInitialized.value) {
                    _wasInitialized.value = false
                }
            }
        }
    }

    // Función para iniciar el cronómetro
    private fun startChrono() {
        chronoJob?.cancel()
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
                delay(100)
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
    private fun resetLists(timersToReset: List<Pair<String, Long>>) {
        _upperList.clear()
        _upperList.addAll(timersToReset)
        _lowerList.clear()
    }

    private fun _startInternalTimer(onTimerFinished: () -> Unit) {
        if (_upperList.isNotEmpty()) {
            _currentTimer.value = _upperList.removeAt(0).second
            _timeRemaining.longValue = _currentTimer.value ?: 0L

            countDownTimer?.cancel()
            countDownTimer = object : CountDownTimer(_timeRemaining.longValue, intervals) {
                override fun onTick(millisUntilFinished: Long) {
                    _timeRemaining.longValue = millisUntilFinished
                }

                override fun onFinish() {
                    onTimerFinished()
                }
            }.start()
            _isRunning.value = true
            _isPaused.value = false
        } else {
            _isRunning.value = false
            _currentTimer.value = null
            _timeRemaining.longValue = 0L
        }
    }

    // Iniciar el temporizador
    fun startTimer(onTimerFinished: () -> Unit) {
        if (!_wasInitialized.value && _timers.value.isNotEmpty()) {
            _wasInitialized.value = true
            startChrono()
        }
        _startInternalTimer {
            val finishedTimer = _currentTimer.value
            if (finishedTimer != null) {
                _lowerList.add(Pair(finishedTimer.toString(), finishedTimer))
                _currentTimer.value = null
                onTimerFinished()
            }

            if (_upperList.isNotEmpty()) {
                startTimer(onTimerFinished)
            } else {

                _isRunning.value = false
                pauseChrono()
                if (currentRepeatLeft > 0) {
                    currentRepeatLeft--
                    resetLists(initialTimers)
                    startTimer(onTimerFinished)
                } else {
                    _wasInitialized.value = true  //si hay bug es aca
                }
            }
        }
    }

    fun onTimerFinish(playSound: () -> Unit) {
        playSound()
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
        _repeatCount.intValue = repeatCount.value
        cancelChrono()
    }

    fun removeAllTimers() {
        viewModelScope.launch {
            timersDataStore.removeAllTimers()
        }
    }

    fun skipCurrentTimer(onTimerFinished: () -> Unit){
        countDownTimer?.cancel()

        _currentTimer.value?.let {
            _lowerList.add(Pair(it.toString(), it))
            _currentTimer.value = null
            onTimerFinished()
        }

        if (_upperList.isNotEmpty()) {
            startTimer(onTimerFinished)
        } else {
            _isRunning.value = false
            pauseChrono()
        }
    }

    fun cycleRepeatCount() {
        _repeatCount.intValue = (_repeatCount.intValue + 1) % 4
    }

    fun resetRepeatCount() {
        _repeatCount.intValue = 0
        currentRepeatLeft = 0
    }

    // Iniciar el temporizador con repetición
    fun startTimerWithRepeat(onTimerFinished: () -> Unit) {
        if (_timers.value.isNotEmpty()) {
            _wasInitialized.value = false
            currentRepeatLeft = _repeatCount.intValue
            resetLists(initialTimers)
            startTimer(onTimerFinished)
        }
    }

}