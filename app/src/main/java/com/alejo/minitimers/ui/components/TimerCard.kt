package com.alejo.minitimers.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.alejo.minitimers.ui.screens.formatTime

@Composable
fun TimerCard(timeText: Long, color: Color , onClick:(() -> Unit)? = null) {

    Surface(
        modifier = Modifier
            .height(124.dp)
            .width(96.dp)
            .let { modifier ->
                if (onClick != null) {
                    modifier.clickable { onClick() }
                } else {
                    modifier
                }
            },
        color = Color.Transparent
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