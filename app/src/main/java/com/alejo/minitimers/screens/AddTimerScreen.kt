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
import com.alejo.minitimers.ui.BottomNavBar
import com.alejo.minitimers.ui.PlusIcon
import com.alejo.minitimers.ui.TimerCard
import com.alejo.minitimers.ui.theme.MiniTimersTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(modifier = Modifier.weight(1f)) {
            TimeSelector()
            Spacer(modifier = Modifier.height(16.dp))
            TimerList()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
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


@Composable
fun TimeSelector() {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }
    var selectedSecond by remember { mutableStateOf(10) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            NumberPickerComponent(
                minValue = 0,
                maxValue = 99,
                selectedValue = selectedHour,
                onValueChange = { newValue ->
                    selectedHour = newValue
                },
                text = "Hours"
            )
            NumberPickerComponent(
                minValue = 0,
                maxValue = 59,
                selectedValue = selectedMinute,
                onValueChange = { newValue ->
                    selectedMinute = newValue
                },
                text = "Min"
            )
            NumberPickerComponent(
                minValue = 0,
                maxValue = 59,
                selectedValue = selectedSecond,
                onValueChange = { newValue ->
                    selectedSecond = newValue
                },
                text = "Sec"
            )


        }
    }
}


@Composable
fun NumberPickerComponent(
    minValue: Int,
    maxValue: Int,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AndroidView(
            modifier = Modifier, // Puedes agregar más modificaciones como el tamaño
            factory = { context ->
                NumberPicker(context).apply {
                    this.minValue = minValue
                    this.maxValue = maxValue
                    this.value = selectedValue
                    this.wrapSelectorWheel = true // Habilitar el scroll circular
                    setOnValueChangedListener { _, _, newVal ->
                        onValueChange(newVal)
                    }
                }
            },
            update = { picker ->
                // Esto se asegura de que se actualice si cambia el valor externamente
                picker.value = selectedValue
            }
        )
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun TimerList() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(max = 300.dp)
            .fillMaxSize()
            .padding(16.dp, 0.dp, 16.dp, 0.dp),// Hacemos que tome solo el espacio necesario
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) { index -> // Reemplaza con tu lista real
            TimerCard(timeText = 5000L, MaterialTheme.colorScheme.primary)
        }
        item {
            Box(contentAlignment = Alignment.TopCenter) {
                PlusIcon(enabled = true, navController = null)
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddTimerScreen() {
    MiniTimersTheme{
        AddTimerScreen(navController = null)
    }
}
