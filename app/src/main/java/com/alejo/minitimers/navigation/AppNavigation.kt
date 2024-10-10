package com.alejo.minitimers.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alejo.minitimers.data.TimersDataStore
import com.alejo.minitimers.screens.AddTimerScreen
import com.alejo.minitimers.screens.MiniTimersScreen

@Composable
fun AppNavigation(timersDataStore: TimersDataStore){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.MiniTimersScreen.route){
        composable(route = AppScreens.MiniTimersScreen.route){
            MiniTimersScreen(navController,timersDataStore)
        }
        composable(route = AppScreens.AddTimerScreen.route){
            AddTimerScreen(navController,timersDataStore)
        }
    }
}
