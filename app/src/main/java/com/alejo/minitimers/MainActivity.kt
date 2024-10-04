package com.alejo.minitimers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alejo.minitimers.ui.MiniTimerScreen
import com.alejo.minitimers.ui.theme.MiniTimersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniTimersTheme {
                MiniTimerScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniTimersApp() {
    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Temporizador Programable") }
            )
        }
    ) { paddingValues -> // paddingValues es necesario para evitar errores de contenido
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp), // Padding adicional
        ) {
            MiniTimerScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MinitimersAppPreview() {
    MiniTimersTheme {
        MiniTimersApp()
    }
}
//MiniTimerScreen()