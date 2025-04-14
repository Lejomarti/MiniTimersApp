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

    var showDialog by remember { mutableStateOf(false) }
//    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

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
                .padding(bottom = 16.dp,top = 16.dp)
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
                                val keyToEdit =
                                    upperList.find { it.second == selectedTime }?.first
                                keyToEdit?.let { key ->
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
                        additionalText = formatTime(elapsedTime + 50)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
                    onClick = {
                        showDialog = true
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
                                if (upperList.isNotEmpty()) {
                                    viewModel.startTimer { soundManager.playSound() }
                                }
                            },
                            enabled = upperList.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.Gray, disabledContentColor = Color.DarkGray)
                        ) {
                            Text(text = "Iniciar")
                        }
                    }

                    if (wasInitialized) {
                        Button(
                            modifier = Modifier.width(180.dp),
                            onClick = {
                                if (isPaused) {
                                    viewModel.resumeTimer { soundManager.playSound() }
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
                            onClick = {
                                viewModel.cancelTimer()
                            }
                        ) {
                            Text(text = "Cancelar")
                        }
                    }
                }
                if(showDialog){
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Eliminar todo", color = Color.Red) },
                        text = { Text("¿Estás seguro de que quieres eliminar todos los temporizadores?") },
                        confirmButton = {
                            TextButton( onClick = {
                                showDialog = false
                                viewModel.removeAllTimers()
                            }) {
                                Text("Si")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("No")
                            }
                        }
                    )
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