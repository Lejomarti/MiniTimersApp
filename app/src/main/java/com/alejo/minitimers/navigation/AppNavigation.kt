package com.alejo.minitimers.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alejo.minitimers.data.SettingsDataStore
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.ui.screens.AddTimerScreen
import com.alejo.minitimers.ui.screens.MiniTimersScreen
import com.alejo.minitimers.ui.screens.SettingsScreen
import com.alejo.minitimers.ui.screens.TimerDetailsScreen
import com.alejo.minitimers.ui.screens.ChronometerScreen

@Composable
fun AppNavigation(timersDataStore: TimersDataStore, settingsDataStore: SettingsDataStore){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.MiniTimersScreen.route){
        composable(route = AppScreens.MiniTimersScreen.route){
            MiniTimersScreen(navController,timersDataStore)
        }
        composable(route = AppScreens.AddTimerScreen.route){
            AddTimerScreen(navController,timersDataStore)
        }
        composable(route = AppScreens.TimerDetailsScreen.route){backStackEntry ->
            val timerId = backStackEntry.arguments?.getString("timerId")
            TimerDetailsScreen(navController, timerId, timersDataStore)
        }
        composable ( route = AppScreens.ChronometerScreen.route) {
          ChronometerScreen(navController)
        }
        composable(route = AppScreens.SettingsScreen.route) {
            SettingsScreen(navController,settingsDataStore)
        }
    }
}
