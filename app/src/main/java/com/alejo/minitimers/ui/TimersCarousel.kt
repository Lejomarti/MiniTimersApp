package com.alejo.minitimers.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alejo.minitimers.data.Timer
import com.alejo.minitimers.data.timersList
import com.alejo.minitimers.navigation.AppNavigation
import com.alejo.minitimers.navigation.AppScreens
import com.alejo.minitimers.screens.formatTime
import com.alejo.minitimers.ui.theme.MiniTimersTheme

fun onAddClick() {
    TODO("Not yet implemented")
}

@Composable
fun TimersCarousel(
    timers: List<Timer>,
    color: Color,
    enabled: Boolean,
    navController: NavController?,
    onClick: (() -> Unit)? = null
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Box(modifier = Modifier
                .width(0.dp)
                .height(60.dp))
        }

        items(timers) { timer ->
            TimerCard(timeText = timer.time, color = color, onClick = onClick)
        }
        item {
            PlusIcon(enabled, navController)
        }
        item {
            Box(modifier = Modifier
                .width(0.dp)
                .height(60.dp))
        }
    }
}

@Composable
fun PlusIcon(enabled: Boolean, navController: NavController?) {
    if (!enabled) {
        Box(
            modifier = Modifier
                .height(124.dp)
                .width(96.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color.LightGray.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Mostrar un anillo sin funcionalidad
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Add Timer",
                    tint = Color.White
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .height(124.dp)
                .width(96.dp)
        ){
        IconButton(
            onClick = {
                navController?.navigate(route = AppScreens.AddTimerScreen.route)
            }, // Acción para añadir temporizador
            modifier = Modifier
                .size(96.dp)
                .background(Color.Gray, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Timer",
                tint = Color.White
            )
        }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MinitimersCarouselPreview() {
    MiniTimersTheme {
//        TimersCarousel(
//            timers = timersList,
//            color = MaterialTheme.colorScheme.primary,
//            enabled = true,
//            navController = null
//        )
        PlusIcon(enabled = true, navController = null)
    }
}

