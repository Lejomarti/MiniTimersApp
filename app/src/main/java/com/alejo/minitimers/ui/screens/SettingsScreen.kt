package com.alejo.minitimers.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alejo.minitimers.R
import com.alejo.minitimers.data.SettingsDataStore
import com.alejo.minitimers.data.SoundList
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.ui.components.BannerAd
import com.alejo.minitimers.ui.components.BottomNavBar
import com.alejo.minitimers.ui.components.ColorDropdownMenu
import com.alejo.minitimers.ui.components.SoundDropdownMenu
import com.alejo.minitimers.ui.components.TopBar
import com.alejo.minitimers.ui.theme.themeColors
import com.alejo.minitimers.ui.viewmodels.TimerViewModel
import com.alejo.minitimers.ui.viewmodels.TimerViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController, timersDataStore: TimersDataStore) {
    val viewModel: TimerViewModel = viewModel(factory = TimerViewModelFactory(timersDataStore))
    val context = LocalContext.current
    val isDarkMode by SettingsDataStore.isDarkMode(context).collectAsState(initial = false)
    val selectedThemeColor by SettingsDataStore.themeColor(context).collectAsState(initial = "Blue")
    val selectedSound by SettingsDataStore.selectedSound(context).collectAsState(initial = "timer")
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar(title = stringResource(R.string.title_settings)) },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.settings_themes),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.settings_DarkMode), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { newValue ->
                        CoroutineScope(Dispatchers.IO).launch {
                            SettingsDataStore.saveDarkMode(context, newValue)
                        }
                    }
                )
            }

            ColorDropdownMenu(
                themeColors = themeColors,
                selectedThemeColor = selectedThemeColor,
                onColorSelected = { colorName ->
                    CoroutineScope(Dispatchers.IO).launch {
                        SettingsDataStore.saveThemeColor(context, colorName)
                    }
                }
            )

            SoundDropdownMenu(
                soundList = SoundList.sounds,
                selectedSound = selectedSound,
                onSoundSelected = { soundName ->
                    CoroutineScope(Dispatchers.IO).launch {
                        SettingsDataStore.saveSelectedSound(context, soundName)
                    }
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Text(
                text = stringResource(R.string.settings_memory),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF811E2A),
                    contentColor = Color.White
                ),
                onClick = {
                    showDialog = true
                }
            ) {
                Text(text = stringResource(R.string.button_delete_All_timer))
            }

            Spacer(modifier = Modifier.weight(1f))
            BannerAd()

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(stringResource(R.string.alert_Deleta_all), color = Color.Red) },
                    text = { Text(stringResource(R.string.alert_confirmation_message)) },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            viewModel.removeAllTimers()
                        }) {
                            Text(stringResource(R.string.alert_Yes))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text(stringResource(R.string.alert_No))
                        }
                    }
                )
            }
        }
    }
}


