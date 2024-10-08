package com.alejo.minitimers.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimerScreen(navController: NavController? ){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir temporizador") },
                navigationIcon = {
                    IconButton(onClick = {navController?.popBackStack()}) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ){
        AddTimerContent(navController)
    }
}

@Composable
fun AddTimerContent(navController: NavController?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hora deslizable
        TimeSelector()

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de temporizadores
        TimerList()

        Spacer(modifier = Modifier.height(16.dp))

        // Botones Añadir y Cancelar
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* Acción de Añadir */ }) {
                Text(text = "Añadir")
            }
            Button(onClick = {navController?.popBackStack()}) {
                Text(text = "Cancelar")
            }
        }
    }
}

@Composable
fun TimeSelector() {
    // Aquí podrías usar sliders o crear tu propio seleccionador de horas
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
//        // Aquí podrías usar un NumberPicker o tu propio componente personalizado
//        Text(text = "00", fontSize = 36.sp)
//        Text(text = "hours", modifier = Modifier.align(Alignment.CenterVertically))
//
//        Text(text = "00", fontSize = 36.sp)
//        Text(text = "mins", modifier = Modifier.align(Alignment.CenterVertically))
//
//        Text(text = "00", fontSize = 36.sp)
//        Text(text = "secs", modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
fun TimerList() {
    // Aquí puedes iterar sobre una lista de temporizadores
    Column {
        repeat(5) { // Reemplaza con tu lista real
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Temporizador de ejemplo",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddTimerScreen() {
    AddTimerScreen(navController = null)
}
