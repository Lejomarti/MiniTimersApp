package com.alejo.minitimers.screens

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alejo.minitimers.ui.BottomNavBar
import com.alejo.minitimers.ui.TimerRing
import com.alejo.minitimers.ui.TopBar
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronometerScreen(navController: NavController) {
    var chronometerWasInitialized by rememberSaveable { mutableStateOf(false) }
    var chronometerIsRunning by rememberSaveable { mutableStateOf(false) }
    var chronometerIsPaused by rememberSaveable { mutableStateOf(false) }
    var startTime by rememberSaveable { mutableStateOf(0L) }
    var elapsedTime by rememberSaveable { mutableStateOf(0L) }
    var progress by remember { mutableFloatStateOf(0f) }

    val coroutineScope = rememberCoroutineScope()

    fun startChronometer() {
        chronometerWasInitialized = true
        chronometerIsRunning = true
        chronometerIsPaused = false
        startTime = SystemClock.elapsedRealtime() - elapsedTime
    }

    fun pauseChronometer() {
        if (chronometerIsRunning) {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            chronometerIsRunning = false
            chronometerIsPaused = true
        }
    }

    fun resumeChronometer() {
        if (chronometerIsPaused) {
            startTime = SystemClock.elapsedRealtime() - elapsedTime
            chronometerIsRunning = true
            chronometerIsPaused = false
        }
    }

    fun cancelChronometer() {
        chronometerWasInitialized = false
        chronometerIsRunning = false
        chronometerIsPaused = false
        elapsedTime = 0L
        progress = 0f
    }

    LaunchedEffect(chronometerIsRunning) {
        if (chronometerIsRunning) {
            while (chronometerIsRunning) {
                elapsedTime = (SystemClock.elapsedRealtime() - startTime)
                progress = (elapsedTime % 60000L/ 6000f)
                delay(10L)
            }
        }
    }

    Scaffold(
        topBar = { TopBar(title = "Chronometer") },
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
                Spacer(modifier = Modifier.height(24.dp))

                val formattedTime = String.format(
                    "%02d:%02d:%02d",
                    (elapsedTime / 1000) / 60,
                    (elapsedTime / 1000) % 60,
                    (elapsedTime % 1000) / 10
                )

                TimerRing(
                    progress = progress,
                    timeText = formattedTime,
                    additionalText = ""
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row() {

                    if (!chronometerWasInitialized) {
                        Button(
                            modifier = Modifier.width(300.dp),
                            onClick = { startChronometer() },
                        ) {
                            Text(text = "Empezar")
                        }
                    } else {
                        Button(
                            modifier = Modifier.width(180.dp),
                            enabled = chronometerIsRunning || chronometerIsPaused,
                            onClick = {
                                if (chronometerIsPaused) resumeChronometer()
                                else pauseChronometer()
                            }
                        ) {
                            Text(text = if (chronometerIsPaused) "Reanudar" else "Pausar")
                        }
                        Button(
                            modifier = Modifier.width(180.dp),
                            onClick = { cancelChronometer() }
                        ) {
                            Text(text = "Cancelar")
                        }
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun TimerOnlyScreenPreview() {
    ChronometerScreen(navController = NavController(LocalContext.current))
}