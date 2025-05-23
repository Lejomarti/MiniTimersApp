package com.alejo.minitimers.ui.screens


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alejo.minitimers.BuildConfig
import com.alejo.minitimers.R
import com.alejo.minitimers.data.SettingsDataStore
import com.alejo.minitimers.data.SoundList
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.navigation.AppScreens
import com.alejo.minitimers.ui.components.BannerAd
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

    val context = LocalContext.current

    val soundManager = remember { SoundManager() }
    val selectedSound by SettingsDataStore.selectedSound(context).collectAsState(initial = "timer")
    val soundOption = SoundList.sounds.find { it.id == selectedSound }
    val resId = soundOption?.resId

    val repeatCount by viewModel.repeatCount

    LaunchedEffect(timers) {
    }
    Scaffold(
        topBar = { TopBar(title = stringResource(R.string.title_miniTimer)) },
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
                    if (BuildConfig.FLAVOR == "free") {BannerAd()}
                    Spacer(modifier = Modifier.weight(1f))
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

                    TimerRing(
                        progress = timeRemaining / (currentTimer ?: 1).toFloat(),
                        timeText = if (!wasInitialized) formatTime(upperList.sumOf { it.second }) else formatTime(
                            timeRemaining
                        ),
                        additionalText = formatTime(elapsedTime + 50),
                        onLongPress = {
                            if (wasInitialized) {
                                viewModel.skipCurrentTimer {
                                    soundManager.playSound(
                                        context,
                                        resId!!
                                    )
                                }
                                Toast.makeText(context, "Timer skipped", Toast.LENGTH_SHORT).show()
                            }
                        },
                        repeatCountIsEnabled = true,
                        repeatCount = repeatCount,
                        onRepeatClick = {
                            viewModel.cycleRepeatCount()
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TimersCarousel(
                        timers = lowerList,
                        color = Color.LightGray,
                        enabled = false,
                        navController = null,
                        onClick = {}
                    )
                }
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
                                viewModel.startTimerWithRepeat {
                                    soundManager.playSound(
                                        context,
                                        resId!!
                                    )
                                }
                            },
                            enabled = upperList.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.DarkGray
                            )
                        ) {
                            Text(text = stringResource(R.string.button_start))
                        }
                    }

                    if (wasInitialized && timeRemaining > 20L) {
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
                            Text(
                                text = if (isPaused) stringResource(R.string.button_resume) else stringResource(
                                    R.string.button_pause
                                )
                            )
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
                            Text(text = stringResource(R.string.button_cancel))
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