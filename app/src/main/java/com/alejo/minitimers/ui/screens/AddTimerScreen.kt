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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alejo.minitimers.R
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.data.personalizedtimersList
import com.alejo.minitimers.ui.components.BottomNavBar
import com.alejo.minitimers.ui.components.TimeSelector
import com.alejo.minitimers.ui.components.TimerList
import kotlinx.coroutines.runBlocking
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimerScreen(navController: NavController,timersDataStore:TimersDataStore) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { stringResource(R.string.title_add_timer) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
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
            AddTimerContent(navController,timersDataStore)
        }
    }
}

@Composable
fun AddTimerContent(navController: NavController, timersDataStore: TimersDataStore) {
    val personalizedTimers by remember { mutableStateOf(personalizedtimersList.toMutableList()) }
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }
    var selectedSecond by remember { mutableStateOf(0) }

    fun setTimeFromMillis(millis: Long) {
        selectedHour = (millis / 3600_000).toInt()
        selectedMinute = ((millis % 3600_000) / 60_000).toInt()
        selectedSecond = ((millis % 60_000) / 1_000).toInt()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            TimeSelector(selectedHour = selectedHour,
                onHourChange = { selectedHour = it },
                selectedMinute = selectedMinute,
                onMinuteChange = { selectedMinute = it },
                selectedSecond = selectedSecond,
                onSecondChange = { selectedSecond = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = androidx.compose.ui.graphics.Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            TimerList(
                personalizedTimers = personalizedTimers,
                onTimerClick = { selectedTimeInMillis ->
                    setTimeFromMillis(selectedTimeInMillis)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier.width(180.dp),
                onClick = {
                    val totalMillis = (selectedHour * 3600 + selectedMinute * 60 + selectedSecond) * 1000L
                    val timerId = "timer_" + UUID.randomUUID().toString()

                    runBlocking {
                        timersDataStore.saveTimer(timerId, totalMillis)
                    }
                    navController.popBackStack()
                }) {
                Text(text = stringResource(R.string.button_add_timer))
            }
            Button(
                modifier = Modifier.width(180.dp),
                onClick = { navController.popBackStack() }) {
                Text(text = stringResource(R.string.button_cancel))
            }
        }
    }
}
