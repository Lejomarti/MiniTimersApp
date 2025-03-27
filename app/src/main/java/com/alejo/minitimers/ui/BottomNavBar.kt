package com.alejo.minitimers.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.alejo.minitimers.ui.theme.MiniTimersTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.alejo.minitimers.navigation.AppScreens


@Composable
fun BottomNavBar(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        Triple("MiniTimer", Icons.Filled.Home, AppScreens.MiniTimersScreen.route),
        Triple("Timer", Icons.Filled.PlayArrow, AppScreens.TimerOnlyScreen.route),
        Triple("Settings", Icons.Filled.Settings, AppScreens.SettingsScreen.route)
    )
        val icons = listOf(Icons.Filled.Home, Icons.Filled.PlayArrow, Icons.Filled.Settings)

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
                        indicatorColor = Color.Transparent // o el color que desees para el indicador
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