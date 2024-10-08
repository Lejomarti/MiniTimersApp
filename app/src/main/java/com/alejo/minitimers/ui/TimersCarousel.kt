package com.alejo.minitimers.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alejo.minitimers.data.Timer
import com.alejo.minitimers.data.timersList
import com.alejo.minitimers.ui.theme.MiniTimersTheme

fun onAddClick() {
    TODO("Not yet implemented")
}

@Composable
fun TimersCarousel(timers: List<Timer>, color: Color, enabled: Boolean) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        items(timers) { timer ->
            TimerCard(timeText = timer.time, color = color)
        }
        item {
            PlusIcon(enabled)
        }
    }
}

@Composable
fun TimerCard(timeText: Long, color: Color) {

    Surface(
        modifier = Modifier
            .height(124.dp)
            .width(96.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(96.dp)
                    .padding(10.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Anillo de color sólido
                    drawArc(
                        color = color,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(18.dp.toPx()) // Grosor del anillo
                    )
                }
            }
            Text(
                text = formatTime(timeText),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}


@Composable
fun PlusIcon(enabled: Boolean) {
    if(!enabled) {
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
    }
    else {
        IconButton(
            onClick = { onAddClick() }, // Acción para añadir temporizador
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


@Preview(showBackground = true)
@Composable
fun MinitimersCarouselPreview() {
    MiniTimersTheme {
        TimersCarousel(
            timers = timersList,
            color = MaterialTheme.colorScheme.primary,
            enabled = true
        )
    }
}

