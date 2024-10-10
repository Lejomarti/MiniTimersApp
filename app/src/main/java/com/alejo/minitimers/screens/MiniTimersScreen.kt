package com.alejo.minitimers.screens

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.alejo.minitimers.data.Timer
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.data.timersList
import com.alejo.minitimers.navigation.AppNavigation
import com.alejo.minitimers.ui.BottomNavBar
import com.alejo.minitimers.ui.TimerRing
import com.alejo.minitimers.ui.TimersCarousel
import com.alejo.minitimers.ui.TopBar
import com.alejo.minitimers.ui.theme.MiniTimersTheme

@Composable
fun MiniTimersScreen(navController: NavController, timersDataStore: TimersDataStore) {
    val timers by timersDataStore.timersFlow.collectAsState(initial = emptyList())

    var wasInitialized by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var countDownTimer: CountDownTimer? by remember { mutableStateOf(null) }

    val upperList = remember { mutableStateListOf<Long>() }
    val lowerList = remember { mutableStateListOf<Long>() }
    var currentTimer by remember { mutableStateOf<Long?>(null) }

    var elapsedTime by remember { mutableStateOf(0L) } // Tiempo del cronómetro
    var isChronoRunning by remember { mutableStateOf(false) }
    var chronoTimer: CountDownTimer? by remember { mutableStateOf(null) }

    // Almacenar el flujo en upperList
    LaunchedEffect(timers) {
        upperList.clear()
        upperList.addAll(timers)
    }

    fun startChrono() {
        if (!isChronoRunning) {
            isChronoRunning = true
            chronoTimer = object : CountDownTimer((timers.sumOf { it }), 100) {
                override fun onTick(millisUntilFinished: Long) {
                    elapsedTime += 100 // Aumenta el tiempo transcurrido en 1000 ms
                }

                override fun onFinish() {
                    chronoTimer?.cancel() // Detener el temporizador
                    isChronoRunning = false
                }
            }.start()
        }
    }

    // Función para pausar el cronómetro
    fun pauseChrono() {
        chronoTimer?.cancel() // Detener el temporizador
        isChronoRunning = false // Actualizar el estado
    }

    // Función para reanudar el cronómetro
    fun resumeChrono() {
        if (!isChronoRunning) {
            startChrono()
        }
    }

    // Función para cancelar el cronómetro
    fun cancelChrono() {
        chronoTimer?.cancel()
        chronoTimer = null
        elapsedTime = 0L
        isChronoRunning = false
    }

    fun resetLists() {
        upperList.clear()
        upperList.addAll(timers)
        lowerList.clear()
    }

    fun onTimerFinish() {
        currentTimer?.let {
            lowerList.add(it)
            currentTimer = null

        }
    }


    fun startTimer() {
        val interval = if (timeRemaining <= 300_000L) 40L else 1000L
        Log.d("alejoIsTalking", "El valor de upperList es $upperList")
        Log.d("alejoIsTalking", "El valor de timers es $upperList")

        if (countDownTimer == null && upperList.isNotEmpty()) {
            wasInitialized = true
            currentTimer = upperList.first() // Tomar el primer valor de upperList
            timeRemaining = currentTimer!! // Asignar el tiempo del temporizador actual
            upperList.removeAt(0)

            countDownTimer = object : CountDownTimer(timeRemaining, interval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeRemaining = millisUntilFinished
                }

                override fun onFinish() {
                    if (upperList.isNotEmpty()) {
                        countDownTimer = null
                        onTimerFinish()
                        startTimer()

                    }
                    if (upperList.isEmpty() && (timeRemaining < 100)) {
                        Log.d("alejoIsTalking", "Esto se llama solo si ya se acaba la lista")
                        timeRemaining = 0L
                        isRunning = false
                        onTimerFinish()
                    }
                }
            }.start()
            isRunning = true
            isPaused = false
        }
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        isRunning = false
        isPaused = true
    }

    fun resumeTimer() {
        if (timeRemaining > 0) {
            val interval = if (timeRemaining <= 300_000L) 40L else 1000L

            countDownTimer = object : CountDownTimer(timeRemaining, interval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeRemaining = millisUntilFinished
                }

                override fun onFinish() {
                    if (upperList.isNotEmpty()) {
                        countDownTimer = null
                        onTimerFinish()
                        startTimer()
                    } else if (upperList.isEmpty() && (timeRemaining < 100)) {
                        timeRemaining = 0L
                        isRunning = false
                        onTimerFinish()
                        Log.d("alejoIsTalking", "Esto se llama solo si ya se acaba la lista")
                    }
                }
            }.start()
            isRunning = true
            isPaused = false // Cambia a estado de ejecución
        }
    }

    // Función para cancelar el temporizador
    fun cancelTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
        timeRemaining = 0L
        isRunning = false
        isPaused = false
        wasInitialized = false
        resetLists()
    }

    // Pantalla del temporizador

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 16.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //primer carousel
                    TimersCarousel(
                        timers = upperList,
                        MaterialTheme.colorScheme.primary,
                        enabled = true,
                        navController
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    TimerRing(
                        progress = timeRemaining / (currentTimer
                            ?: 1).toFloat(), // Evitar división por cero
                        timeText =
                        when {
                            !wasInitialized -> formatTime(upperList.sumOf { it }) // Mostrar suma si no se ha inicializado
                            else -> formatTime(timeRemaining) // Mostrar tiempo restante si se ha inicializado
                        },
                        additionalText = formatTime(elapsedTime) // Segundo texto
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    //segundo carousel
                    TimersCarousel(
                        timers = lowerList,
                        color = Color.LightGray,
                        enabled = false,
                        navController = null
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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
                                    resumeTimer() // Llama a reanudar si está pausado
                                    startChrono()
                                } else {
                                    pauseTimer() // Pausa si está en ejecución
                                    pauseChrono()
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
                                cancelChrono()
                            }) {
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
    MiniTimersTheme {
//        AppNavigation()
    }
}