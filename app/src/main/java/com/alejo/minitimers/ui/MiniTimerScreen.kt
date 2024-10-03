package com.alejo.minitimers.ui

import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

@Composable
fun MiniTimerScreen(

) {
    var HardCodedTime = 10000L

    var timeRemaining by remember { mutableStateOf(HardCodedTime) }
    var isRunning by remember { mutableStateOf(false) }
    var countDownTimer: CountDownTimer? by remember { mutableStateOf(null) }

    fun startTimer() {
        if (countDownTimer == null) {
            countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeRemaining = millisUntilFinished
                }

                override fun onFinish() {
                    timeRemaining = 0L
                    isRunning = false
                }
            }.start()
            isRunning = true
        }
    }

    // Función para pausar el temporizador
    fun pauseTimer() {
        countDownTimer?.cancel()
        countDownTimer = null // Reiniciar el temporizador
        isRunning = false
    }

    // Función para cancelar el temporizador
    fun cancelTimer() {
        countDownTimer?.cancel()
        countDownTimer = null // Reiniciar el temporizador
        timeRemaining = HardCodedTime // Volver al tiempo inicial
        isRunning = false
    }

    // Pantalla del temporizador
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostrar el tiempo restante en formato mm:ss
        Text(text = formatTime(timeRemaining), style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (!isRunning) startTimer()
                },
                enabled = !isRunning
            ) {
                Text(text = "Iniciar")
            }

            Button(
                onClick = {
                    if (isRunning) pauseTimer()
                },
                enabled = isRunning
            ) {
                Text(text = "Pausar")
            }

            Button(onClick = { cancelTimer() }) {
                Text(text = "Cancelar")
            }
        }
    }
}

// Función auxiliar para formatear el tiempo en mm:ss
fun formatTime(timeMillis: Long): String {
    val minutes = (timeMillis / 1000) / 60
    val seconds = (timeMillis / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}