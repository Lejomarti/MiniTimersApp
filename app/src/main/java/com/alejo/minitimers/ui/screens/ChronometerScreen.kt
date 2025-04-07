package com.alejo.minitimers.ui.screens

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alejo.minitimers.data.ChronometerDataStore
import com.alejo.minitimers.ui.components.BottomNavBar
import com.alejo.minitimers.ui.components.TimerRing
import com.alejo.minitimers.ui.components.TopBar
import com.alejo.minitimers.ui.viewmodels.ChronometerViewModel
import com.alejo.minitimers.ui.viewmodels.ChronometerViewModelFactory


@Composable
fun ChronometerScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ChronometerViewModel = viewModel(
        factory = ChronometerViewModelFactory(ChronometerDataStore(context))
    )

    val chronometerWasInitialized by viewModel.chronometerWasInitialized.collectAsState()
    val chronometerIsRunning by viewModel.chronometerIsRunning.collectAsState()
    val chronometerIsPaused by viewModel.chronometerIsPaused.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val progress by viewModel.progress.collectAsState()

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
                modifier = Modifier.fillMaxSize(),
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
                            onClick = { viewModel.startChronometer() },
                        ) {
                            Text(text = "Empezar")
                        }
                    } else {
                        Button(
                            modifier = Modifier.width(180.dp),
                            enabled = chronometerIsRunning || chronometerIsPaused,
                            onClick = {
                                if (chronometerIsPaused) viewModel.resumeChronometer()
                                else viewModel.pauseChronometer()
                            }
                        ) {
                            Text(text = if (chronometerIsPaused) "Reanudar" else "Pausar")
                        }
                        Button(
                            modifier = Modifier.width(180.dp),
                            onClick = { viewModel.cancelChronometer() }
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