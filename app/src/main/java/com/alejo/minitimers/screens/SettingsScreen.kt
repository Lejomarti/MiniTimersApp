package com.alejo.minitimers.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alejo.minitimers.data.SettingsDataStore
import com.alejo.minitimers.ui.BottomNavBar
import com.alejo.minitimers.ui.TopBar
import com.alejo.minitimers.ui.theme.themeColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController, settingsDataStore: SettingsDataStore) {
    val isDarkMode by settingsDataStore.isDarkMode.collectAsState(initial = false)
    val selectedThemeColor by settingsDataStore.themeColor.collectAsState(initial = "Blue")


    var expanded by remember { mutableStateOf(false) }

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

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Color del tema", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { expanded = true }) {
                        Text(text = selectedThemeColor)
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    themeColors.keys.forEach { colorName ->
                        DropdownMenuItem(
                            text = { Text(colorName) },
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    settingsDataStore.saveThemeColor(colorName)
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


//@Preview
//@Composable
//fun SettingsScreenPreview() {
////    SettingsScreen(navController = NavController(LocalContext.current))
//}

