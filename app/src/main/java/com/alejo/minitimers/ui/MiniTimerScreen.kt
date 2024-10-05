package com.alejo.minitimers.ui

import android.os.CountDownTimer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.alejo.minitimers.data.Timer
import com.alejo.minitimers.data.timersList
import com.alejo.minitimers.ui.theme.MiniTimersTheme

@Composable
fun MiniTimerScreen(

) {
    val HardCodedTime = 10L

    var timeRemaining by remember { mutableStateOf(HardCodedTime) }
    var isRunning by remember { mutableStateOf(false) }
    var countDownTimer: CountDownTimer? by remember { mutableStateOf(null) }

    val upperList = remember { mutableStateListOf(*timersList.toTypedArray()) }
    val lowerList = remember { mutableStateListOf<Timer>() }


    var currentTimer by remember { mutableStateOf(upperList.firstOrNull()) }

    fun startTimer() {
        if (countDownTimer == null) {
            val interval = if (timeRemaining <= 300_000L) 40L else 1000L

            countDownTimer = object : CountDownTimer(timeRemaining, interval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeRemaining = millisUntilFinished
                }

                override fun onFinish() {
                    timeRemaining = 0L
                    isRunning = false
                    onTimerFinish()
                }
            }.start()
            isRunning = true
        }
    }

    // Función para pausar el temporizador
    fun pauseTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
        isRunning = false
    }

    // Función para cancelar el temporizador
    fun cancelTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
        timeRemaining = HardCodedTime
        isRunning = false
    }



    fun startNextTimer() {  //1
        if (upperList.isNotEmpty()) {
            currentTimer = upperList.first()
            upperList.removeAt(0)
            startTimer() // Lógica para iniciar el temporizador
        }
    }

    fun onTimerFinish() {
        if (currentTimer != null) {
            lowerList.add(currentTimer!!)
            currentTimer = null
            startNextTimer() // Iniciar el siguiente temporizador
        }
    }


    // Pantalla del temporizador
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //primer carousel
        TimersCarousel(timers = upperList, MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))

        TimerRing(
            progress = timeRemaining / HardCodedTime.toFloat(),
            timeText = formatTime(timeRemaining),
            additionalText = "00:00:00" // Segundo texto
        )
        Spacer(modifier = Modifier.height(24.dp))

        //segundo carousel
        TimersCarousel(timers = lowerList, color = Color.LightGray)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (!isRunning) startNextTimer()
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

@Composable
fun TimerRing(progress: Float, timeText: String, additionalText: String) {
    val ringColor = MaterialTheme.colorScheme.primary

    // Canvas para dibujar el anillo progresivo
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(300.dp) // Tamaño del anillo
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Anillo de fondo
            drawArc(
                color = Color.Gray,
                startAngle = 270f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(16.dp.toPx(), cap = StrokeCap.Round)
            )

            // Anillo de progreso
            drawArc(
                color = ringColor,
                startAngle = 270f,
                sweepAngle = 360f * progress, // Progreso en base al tiempo restante
                useCenter = false,
                style = Stroke(16.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Texto dentro del anillo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeText,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            Text(
                text = additionalText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}


fun formatTime(timeMillis: Long): String {
    val hours = (timeMillis / 1000) / 3600
    val minutes = (timeMillis / 1000) / 60
    val seconds = (timeMillis / 1000) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

@Preview(showBackground = true)
@Composable
fun MinitimerScreenPreview() {
    MiniTimersTheme {
        MiniTimerScreen()
    }
}