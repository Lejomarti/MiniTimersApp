package com.alejo.minitimers.screens

import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.navigation.AppScreens
import com.alejo.minitimers.ui.BottomNavBar
import com.alejo.minitimers.ui.TimerRing
import com.alejo.minitimers.ui.TimersCarousel
import com.alejo.minitimers.ui.TopBar
import com.alejo.minitimers.utils.SoundManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun MiniTimersScreen(navController: NavController, timersDataStore: TimersDataStore) {
    val timers by timersDataStore.timersFlow.collectAsState(initial = emptyList())

    var wasInitialized by rememberSaveable { mutableStateOf(false) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var isPaused by rememberSaveable { mutableStateOf(false) }

    var startTime by remember { mutableStateOf(0L) } // Marca de inicio del cronómetro
    var pausedTime by remember { mutableStateOf(0L) } // Tiempo acumulado en pausa
    var elapsedTime by remember { mutableStateOf(0L) }

    var upperList = remember { mutableStateListOf<Pair<String, Long>>() }
    var lowerList = remember { mutableStateListOf<Long>() }
    var currentTimer by remember { mutableStateOf<Long?>(null) }

    var timeRemaining by rememberSaveable { mutableStateOf(0L) }
    var countDownTimer: CountDownTimer? by remember { mutableStateOf(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }
    val intervals :Long = 10

    // Función para iniciar el cronómetro
    fun startChrono() {
        if (isPaused) {
            startTime = SystemClock.elapsedRealtime() - pausedTime
        } else {
            startTime = SystemClock.elapsedRealtime()
        }
        isPaused = false

        scope.launch {
            while (isRunning) {
                elapsedTime = SystemClock.elapsedRealtime() - startTime
                delay(100) // Actualización frecuente
            }
        }
    }

    // Pausar el cronómetro
    fun pauseChrono() {
        isPaused = true
        pausedTime = SystemClock.elapsedRealtime() - startTime

    }

    // Reanudar el cronómetro
    fun resumeChrono() {
        if (isPaused) {
            startChrono()
        }
    }

    // Cancelar el cronómetro
    fun cancelChrono() {
        elapsedTime = 0L
        startTime = 0L
        pausedTime = 0L
    }

    // Resetear las listas
    fun resetLists() {
        upperList.clear()
        upperList.addAll(timers)
        lowerList.clear()
    }


    // Iniciar el temporizador
    fun startTimer() {
        if (upperList.isNotEmpty()) {
            if (!wasInitialized) {
                wasInitialized = true
                isRunning = true
                startChrono() // Solo inicia el cronómetro una vez al principio
            }
            currentTimer = upperList.removeAt(0).second
            timeRemaining = currentTimer ?: 0L


            countDownTimer?.cancel() // Cancela cualquier temporizador previo
            countDownTimer = object : CountDownTimer(timeRemaining, intervals) {
                override fun onTick(millisUntilFinished: Long) {
                    timeRemaining = millisUntilFinished
                }

                override fun onFinish() {
                    currentTimer?.let {
                        lowerList.add(it)
                        currentTimer = null
                        soundManager.playSound()
                    }

                    if (upperList.isNotEmpty()) {
                        startTimer() // Continúa con el siguiente temporizador sin tocar el cronómetro
                    } else {
                        isRunning = false
                        pauseChrono() // Solo se pausa cuando todos los timers terminan
                    }
                }
            }.start()
        } else {
            isRunning = false
            wasInitialized = false
            pauseChrono() // Se detiene el cronómetro solo si ya no hay timers
        }
    }

    fun onTimerFinish() {
        currentTimer?.let {
            lowerList.add(it)
            currentTimer = null
            soundManager.playSound()
        }

        if (upperList.isNotEmpty()) {
            startTimer() // Continúa con el siguiente temporizador sin tocar el cronómetro
        } else {
            isRunning = false
            pauseChrono()
        }
    }

    // Pausar el temporizador
    fun pauseTimer() {
        countDownTimer?.cancel()
        isRunning = false
        isPaused = true
        pauseChrono()
    }

    // Reanudar el temporizador
    fun resumeTimer() {
        isRunning = true
        isPaused = false
        resumeChrono()

        countDownTimer = object : CountDownTimer(timeRemaining, 10) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
            }

            override fun onFinish() {
                onTimerFinish()
            }
        }.start()
    }

    // Cancelar el temporizador
    fun cancelTimer() {
        pauseTimer()
        wasInitialized = false
        isRunning = false
        isPaused = false
        timeRemaining = 0L
        currentTimer = 0L
        resetLists()
        cancelChrono()
        }


    LaunchedEffect(timers) {
        resetLists()
    }

    // UI
    Scaffold(
        topBar = { TopBar("Mini Timer") },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Primer carrusel
                    TimersCarousel(
                        timers = upperList.map { it.second },
                        MaterialTheme.colorScheme.primary,
                        enabled = !wasInitialized,
                        navController,
                        onClick = { selectedTime ->
                            if (!wasInitialized) {
                                val keyToDelete =
                                    upperList.find { it.second == selectedTime }?.first
                                keyToDelete?.let { key ->
                                    navController.navigate(
                                        AppScreens.TimerDetailsScreen.createRoute(
                                            key
                                        )
                                    )
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Anillo de temporizador
                    TimerRing(
                        progress = timeRemaining / (currentTimer ?: 1).toFloat(),
                        timeText = if (!wasInitialized) formatTime(upperList.sumOf { it.second }) else formatTime(
                            timeRemaining
                        ),
                        additionalText = formatTime(elapsedTime + 50) // Segundo texto: cronómetro
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Segundo carrusel
                    TimersCarousel(
                        timers = lowerList,
                        color = Color.LightGray,
                        enabled = false,
                        navController = null,
                        onClick = {}
                    )
                }

                Button(
                    modifier = Modifier.width(300.dp),
                    onClick = {
                        scope.launch {
                            timersDataStore.removeAllTimers()
                        }
                        cancelChrono()
                    }
                ) {
                    Text(text = "Eliminar todo")
                }

                // Botones de control
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (!wasInitialized) {
                        Button(
                            modifier = Modifier.width(300.dp),
                            onClick = {
                                if (!isRunning && !isPaused) {
                                    startTimer()
                                    startChrono()
                                }
                            },
                            enabled = !wasInitialized
                        ) {
                            Text(text = "Iniciar")
                        }
                    }

                    if (wasInitialized) {
                        Button(
                            modifier = Modifier.width(180.dp),
                            onClick = {
                                if (isPaused) {
                                    resumeTimer()
                                } else {
                                    pauseTimer()
                                }
                            },
                            enabled = isRunning || isPaused
                        ) {
                            Text(text = if (isPaused) "Reanudar" else "Pausar")
                        }
                    }

                    if (wasInitialized) {
                        Button(
                            modifier = Modifier.width(180.dp),
                            onClick = {
                                cancelTimer()
                            }
                        ) {
                            Text(text = "Cancelar")
                        }
                    }
                }
            }
        }
    }
}

fun formatTime(timeMillis: Long): String {
    val hours = (timeMillis / 1000) / 3600
    val minutes = ((timeMillis / 1000) % 3600) / 60
    val seconds = (timeMillis / 1000) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

@Preview(showBackground = true)
@Composable
fun MinitimerScreenPreview() {
    val navController = rememberNavController()
    MiniTimersScreen(navController, TimersDataStore(navController.context))
}