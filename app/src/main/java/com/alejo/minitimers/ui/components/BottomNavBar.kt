package com.alejo.minitimers.ui.components

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.alejo.minitimers.navigation.AppScreens


@SuppressLint("SuspiciousIndentation")
@Composable
fun BottomNavBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        Triple("MiniTimer", Icons.Filled.Home, AppScreens.MiniTimersScreen.route),
        Triple("Chronometer", Icons.Filled.PlayArrow, AppScreens.ChronometerScreen.route),
        Triple("Settings", Icons.Filled.Settings, AppScreens.SettingsScreen.route)
    )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            items.forEach { (label, icon, route) ->
                NavigationBarItem(
                    icon = {Icon(imageVector = icon, contentDescription = label)},
                    label = { Text(label) },
                    selected = currentRoute == route,
                    onClick = {
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(AppScreens.MiniTimersScreen.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    },
                        colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun BottomNavBarPreview() {
//    MiniTimersTheme {
//    }
//}