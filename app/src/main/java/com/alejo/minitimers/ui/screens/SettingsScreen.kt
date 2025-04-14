package com.alejo.minitimers.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alejo.minitimers.data.SettingsDataStore
import com.alejo.minitimers.ui.components.BottomNavBar
import com.alejo.minitimers.ui.components.ColorDropdownMenu
import com.alejo.minitimers.ui.components.TopBar
import com.alejo.minitimers.ui.theme.themeColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val isDarkMode by SettingsDataStore.isDarkMode(context).collectAsState(initial = false)
    val selectedThemeColor by SettingsDataStore.themeColor(context).collectAsState(initial = "Blue")

    Scaffold(
        topBar = { TopBar(title = "Settings") },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            // Switch para activar/desactivar modo oscuro
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "DarkMode", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { newValue ->
                        // Guardar el nuevo valor en DataStore
                        CoroutineScope(Dispatchers.IO).launch {
                            SettingsDataStore.saveDarkMode(context, newValue)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ColorDropdownMenu(
                themeColors = themeColors,
                selectedThemeColor = selectedThemeColor,
                onColorSelected = { colorName ->
                    CoroutineScope(Dispatchers.IO).launch {
                        SettingsDataStore.saveThemeColor(context, colorName)
                    }
                }
            )
        }
    }
}


