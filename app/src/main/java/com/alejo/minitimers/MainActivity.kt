package com.alejo.minitimers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.alejo.minitimers.data.SettingsDataStore
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.navigation.AppNavigation
import com.alejo.minitimers.ui.theme.MiniTimersTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        enableEdgeToEdge()
        val timersDataStore = TimersDataStore(this)
        setContent {
            val isDarkMode by SettingsDataStore.isDarkMode(context = this).collectAsState(initial = false)
            val themeColorName by SettingsDataStore.themeColor(context = this).collectAsState(initial = "Blue")
            MiniTimersTheme(darkTheme = isDarkMode, themeColorName = themeColorName) {
                MiniTimersApp(timersDataStore)
            }
        }
    }
}


@Composable
fun MiniTimersApp(timersDataStore: TimersDataStore) {
            AppNavigation(timersDataStore = timersDataStore)
}
