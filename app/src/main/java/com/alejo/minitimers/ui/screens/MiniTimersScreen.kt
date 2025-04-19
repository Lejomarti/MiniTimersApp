package com.alejo.minitimers.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alejo.minitimers.data.SettingsDataStore
import com.alejo.minitimers.data.SoundList
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.navigation.AppScreens
import com.alejo.minitimers.ui.components.BottomNavBar
import com.alejo.minitimers.ui.components.TimerRing
import com.alejo.minitimers.ui.components.TimersCarousel
import com.alejo.minitimers.ui.components.TopBar
import com.alejo.minitimers.ui.viewmodels.TimerViewModel
import com.alejo.minitimers.ui.viewmodels.TimerViewModelFactory
import com.alejo.minitimers.utils.Sounds.SoundManager

@Composable
fun MiniTimersScreen(navController: NavController, timersDataStore: TimersDataStore) {

    val viewModel: TimerViewModel = viewModel(factory = TimerViewModelFactory(timersDataStore))

    val timers by viewModel.timers.collectAsStateWithLifecycle(initialValue = emptyList())
    val wasInitialized by viewModel.wasInitialized
    val isRunning by viewModel.isRunning
    val isPaused by viewModel.isPaused
    val elapsedTime by viewModel.elapsedTime
    val upperList = viewModel.upperList
    val lowerList = viewModel.lowerList
    val currentTimer by viewModel.currentTimer
    val timeRemaining by viewModel.timeRemaining

//    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val soundManager = remember { SoundManager() }
    val selectedSound by SettingsDataStore.selectedSound(context).collectAsState(initial = "timer")
    val soundOption = SoundList.sounds.find { it.id == selectedSound }
    val resId = soundOption?.resId

    LaunchedEffect(timers) {
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
                .padding(bottom = 16.dp, top = 16.dp)
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
                        timers = upperList,
                        MaterialTheme.colorScheme.primary,
                        enabled = !wasInitialized,
                        navController,
                        onClick = { timerId ->
                            if (!wasInitialized) {
                                navController.navigate(
                                    AppScreens.TimerDetailsScreen.createRoute(timerId)
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    // Anillo de temporizador
                    TimerRing(
                        progress = timeRemaining / (currentTimer ?: 1).toFloat(),
                        timeText = if (!wasInitialized) formatTime(upperList.sumOf { it.second }) else formatTime(
                            timeRemaining
                        ),
                        additionalText = formatTime(elapsedTime + 50)
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    // Segundo carrusel
                    TimersCarousel(
                        timers = lowerList,
                        color = Color.LightGray,
                        enabled = false,
                        navController = null,
                        onClick = {}
                    )
                }
                // Botones de control
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (!wasInitialized) {
                        Button(
                            modifier = Modifier.width(300.dp),
                            onClick = {
                                if (upperList.isNotEmpty()) {
                                    viewModel.startTimer {
                                        resId?.let {
                                            soundManager.playSound(context, it)
                                        }
                                    }
                                }
                            },
                            enabled = upperList.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.DarkGray
                            )
                        ) {
                            Text(text = "Iniciar")
                        }
                    }

                    if (wasInitialized) {
                        Button(
                            modifier = Modifier.width(180.dp),
                            onClick = {
                                if (isPaused) {
                                    viewModel.resumeTimer {
                                        resId?.let {
                                            soundManager.playSound(context, it)
                                        }
                                    }
                                } else {
                                    viewModel.pauseTimer()
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF811E2A)),
                            onClick = {
                                viewModel.cancelTimer()
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