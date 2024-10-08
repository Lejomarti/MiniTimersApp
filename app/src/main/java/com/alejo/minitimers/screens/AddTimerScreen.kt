package com.alejo.minitimers.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alejo.minitimers.ui.BottomNavBar
import com.alejo.minitimers.ui.PlusIcon
import com.alejo.minitimers.ui.TimerCard

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
        modifier = Modifier
            .fillMaxSize(),
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
            Button(onClick = { navController?.popBackStack() }) {
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
        // Aquí podrías usar un NumberPicker o tu propio componente personalizado
        Text(text = "00", fontSize = 36.sp)
        Text(
            text = "hours",
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.labelSmall
        )

        Text(text = "00", fontSize = 36.sp)
        Text(
            text = "mins",
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.labelSmall
        )

        Text(text = "00", fontSize = 36.sp)
        Text(
            text = "secs",
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun TimerList() {
    // Aquí puedes iterar sobre una lista de temporizadores
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // Establece 3 columnas
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 0.dp), // Ajusta el padding si es necesario
        contentPadding = PaddingValues(8.dp), // Padding dentro de la grilla
        verticalArrangement = Arrangement.spacedBy(16.dp), // Espacio vertical entre los elementos
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio horizontal entre los elementos
    ) {
        items(5) { index -> // Reemplaza con tu lista real
            TimerCard(timeText = 5000L, MaterialTheme.colorScheme.primary)
        }
        item {
            Box(contentAlignment = Alignment.TopCenter){
            PlusIcon(enabled = true, navController = null) }
            }
    }
}

@Preview
@Composable
fun PreviewAddTimerScreen() {
    AddTimerScreen(navController = null)
}
