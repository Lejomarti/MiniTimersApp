package com.alejo.minitimers.screens

import android.annotation.SuppressLint
import android.widget.NumberPicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.alejo.minitimers.data.personalizedtimersList
import com.alejo.minitimers.ui.BottomNavBar
import com.alejo.minitimers.ui.NumberPickerComponent
import com.alejo.minitimers.ui.PlusIcon
import com.alejo.minitimers.ui.TimeSelector
import com.alejo.minitimers.ui.TimerCard
import com.alejo.minitimers.ui.TimerList
import com.alejo.minitimers.ui.theme.MiniTimersTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimerScreen(navController: NavController?) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir temporizador") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }, bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 16.dp)

        ) {
            AddTimerContent(navController)
        }
    }
}

@Composable
fun AddTimerContent(navController: NavController?) {
    var personalizedTimers by remember { mutableStateOf(personalizedtimersList.toMutableList()) }
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

        Column(modifier = Modifier.weight(1f)) {
            TimeSelector(selectedHour = selectedHour,
                onHourChange = { selectedHour = it },
                selectedMinute = selectedMinute,
                onMinuteChange = { selectedMinute = it },
                selectedSecond = selectedSecond,
                onSecondChange = { selectedSecond = it }
            )
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
                onClick = { /* Acción de Añadir */ }) {
                Text(text = "Añadir")
            }
            Button(
                modifier = Modifier.width(180.dp),
                onClick = { navController?.popBackStack() }) {
                Text(text = "Cancelar")
            }
        }
    }
}


@Preview
@Composable
fun PreviewAddTimerScreen() {
    MiniTimersTheme {
        AddTimerScreen(navController = null)
    }
}
