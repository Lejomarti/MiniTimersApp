package com.alejo.minitimers.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alejo.minitimers.R
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.ui.components.BottomNavBar
import com.alejo.minitimers.ui.components.TimeSelector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerDetailsScreen(
    navController: NavController,
    timerId: String?,
    timersDataStore: TimersDataStore
) {
    var timerValue by remember { mutableStateOf<Long?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(timerId) {
        timerId?.let {
            timerValue = timersDataStore.getTimerById(it)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { stringResource(R.string.title_edit_timer) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }, bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 16.dp)

        ) {
            timerValue?.let {
                EditTimerContent(navController, timerId, timerValue!!, timersDataStore, scope)
            }
        }
    }
}

@Composable
fun EditTimerContent(
    navController: NavController,
    timerId: String?,
    initialTime: Long?,
    timersDataStore: TimersDataStore,
    scope: CoroutineScope
) {

    var selectedHour by remember { mutableStateOf((initialTime!! / 3600_000).toInt()) }
    var selectedMinute by remember { mutableStateOf(((initialTime!! % 3600_000) / 60_000).toInt()) }
    var selectedSecond by remember { mutableStateOf(((initialTime!! % 60_000) / 1_000).toInt()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        TimeSelector(
            selectedHour = selectedHour,
            onHourChange = { selectedHour = it },
            selectedMinute = selectedMinute,
            onMinuteChange = { selectedMinute = it },
            selectedSecond = selectedSecond,
            onSecondChange = { selectedSecond = it }
        )

        Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        )
        {
            Button(onClick = {
                val newTime =
                    (selectedHour * 3600_000L + selectedMinute * 60_000L + selectedSecond * 1_000L)
                timerId?.let { id ->
                    scope.launch {
                        timersDataStore.updateTimer(id, newTime)
                        navController.popBackStack()
                    }
                }

            }) {
                Text(stringResource(R.string.button_save))
            }

            Button(
                colors = ButtonDefaults.buttonColors(Color.Red),
                onClick = {
                    timerId?.let { id ->
                        scope.launch {
                            timersDataStore.removeTimer(id)
                            navController.popBackStack()
                        }

                    }
                }

            ) {
                Text(stringResource(R.string.button_delete_timer))
            }
        }
    }
}
