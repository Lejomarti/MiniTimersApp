package com.alejo.minitimers.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alejo.minitimers.screens.AddTimerScreen
import com.alejo.minitimers.screens.MiniTimersScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.MiniTimersScreen.route){
        composable(route = AppScreens.MiniTimersScreen.route){
            MiniTimersScreen(navController)
        }
        composable(route = AppScreens.AddTimerScreen.route){
            AddTimerScreen(navController)
        }
    }
}
