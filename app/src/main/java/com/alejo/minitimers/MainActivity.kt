package com.alejo.minitimers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.alejo.minitimers.data.SettingsDataStore
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.navigation.AppNavigation
import com.alejo.minitimers.ui.theme.MiniTimersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val timersDataStore = TimersDataStore(this)
        val settingsDataStore = SettingsDataStore(this)
        setContent {
            val isDarkMode by settingsDataStore.isDarkMode.collectAsState(initial = false)
            MiniTimersTheme(darkTheme = isDarkMode) {
                MiniTimersApp(timersDataStore,settingsDataStore)
            }
        }
    }
}


@Composable
fun MiniTimersApp(timersDataStore: TimersDataStore, settingsDataStore: SettingsDataStore) {
//    Scaffold(
//        bottomBar = { BottomNavBar() },
//    ) { paddingValues -> // paddingValues es necesario para evitar errores de contenido
//        Surface(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
            AppNavigation(timersDataStore = timersDataStore,settingsDataStore = settingsDataStore)
//        }
//    }
}

@Preview(showBackground = true)
@Composable
fun MinitimersAppPreview() {
    MiniTimersTheme {
//        MiniTimersApp(timersDataStore)
    }
}
