package com.alejo.minitimers.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.alejo.minitimers.ui.theme.MiniTimersTheme

@Composable
fun TimersCarousel(timers: List<Long>,color: Color) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        items(timers) { time ->
            TimerCard(timeText = time,color = color)

        }
    }
}

@Composable
fun TimerCard(timeText: Long,color: Color) {

    Surface(modifier = Modifier
        .height(124.dp)
        .width(96.dp)) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(96.dp).padding(10.dp)
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

@Preview(showBackground = true)
@Composable
fun MinitimersCarouselPreview() {
    MiniTimersTheme {
        TimersCarousel(timers = listOf(300000L, 600000L, 900000L),color = MaterialTheme.colorScheme.primary)
    }
}
