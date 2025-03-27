package com.alejo.minitimers.navigation

sealed class AppScreens(val route: String) {
    object MiniTimersScreen : AppScreens ("mini_timer_screen")
    object AddTimerScreen : AppScreens ("Add_timer_screen")
    object TimerDetailsScreen : AppScreens("timer_details_screen/{timerId}") {
        fun createRoute(timerId: String) = "timer_details_screen/$timerId"
    }

    object TimerOnlyScreen : AppScreens("timer_only_screen")
    object SettingsScreen : AppScreens("settings_screen")
}