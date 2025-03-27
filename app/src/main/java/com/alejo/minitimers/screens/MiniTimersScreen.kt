package com.alejo.minitimers.screens

import android.os.CountDownTimer
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
import kotlinx.coroutines.launch


@Composable
fun MiniTimersScreen(navController: NavController, timersDataStore: TimersDataStore) {
    val timers by timersDataStore.timersFlow.collectAsState(initial = emptyList())

    var wasInitialized by rememberSaveable { mutableStateOf(false) }
    var timeRemaining by rememberSaveable { mutableStateOf(0L) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var countDownTimer: CountDownTimer? by remember { mutableStateOf(null) }

    val upperList = remember { mutableStateListOf<Pair<String, Long>>() }
    val lowerList = remember { mutableStateListOf<Long>() }
    var currentTimer by remember { mutableStateOf<Long?>(null) }

    var elapsedTime by remember { mutableStateOf(0L) } // Tiempo del cronómetro
    var isChronoRunning by remember { mutableStateOf(false) }
    var chronoTimer: CountDownTimer? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

    // Almacenar el flujo en upperList
    LaunchedEffect(timers) {
        upperList.clear()
        upperList.addAll(timers)
    }

    fun startChrono() {
        if (!isChronoRunning) {
            isChronoRunning = true
            chronoTimer = object : CountDownTimer((timers.sumOf { it.second }), 100) {
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
        chronoTimer?.cancel()
        isChronoRunning = false
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

            soundManager.playSound()
        }
    }


    fun startTimer() {
        val interval = if (timeRemaining <= 300_000L) 40L else 1000L
        Log.d("alejoIsTalking", "El valor de upperList es $upperList")
        Log.d("alejoIsTalking", "El valor de timers es $upperList")

        if (countDownTimer == null && upperList.isNotEmpty()) {
            wasInitialized = true
            currentTimer = upperList.map { it.second }.first()
            timeRemaining = currentTimer!!
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
            isPaused = false
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
                        timers = upperList.map { it.second },
                        MaterialTheme.colorScheme.primary,
                        enabled = !wasInitialized,
                        navController,
                        onClick = { selectedTime ->
                            if (!wasInitialized) {
                                val keyToDelete = upperList.find { it.second == selectedTime }?.first
                                keyToDelete?.let { key ->
                                    Log.d("alejoIsTalking", "Se ha pulsado en $key")
                                    navController.navigate(AppScreens.TimerDetailsScreen.createRoute(key))
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    TimerRing(
                        progress = timeRemaining / (currentTimer
                            ?: 1).toFloat(),
                        timeText =
                        when {
                            !wasInitialized -> formatTime(upperList.sumOf { it.second })
                            else -> formatTime(timeRemaining)
                        },
                        additionalText = formatTime(elapsedTime) // Segundo texto
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    //segundo carousel
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
                    },
                ) {
                    Text(text = "Eliminar todo")
                }

                //botones inferiores
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
    val navController = rememberNavController()
    MiniTimersScreen(navController, TimersDataStore(navController.context))
}