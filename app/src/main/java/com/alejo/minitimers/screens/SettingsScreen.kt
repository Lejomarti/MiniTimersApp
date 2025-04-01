package com.alejo.minitimers.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alejo.minitimers.data.SettingsDataStore
import com.alejo.minitimers.ui.BottomNavBar
import com.alejo.minitimers.ui.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController, settingsDataStore: SettingsDataStore) {
    val isDarkMode by settingsDataStore.isDarkMode.collectAsState(initial = false)

    Scaffold(
        topBar = { TopBar(title = "Settings") },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 16.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // Switch para activar/desactivar modo oscuro
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Modo oscuro", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { newValue ->
                            // Guardar el nuevo valor en DataStore
                            CoroutineScope(Dispatchers.IO).launch {
                                settingsDataStore.saveDarkMode(newValue)
                            }
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun SettingsScreenPreview() {
//    SettingsScreen(navController = NavController(LocalContext.current))
}

