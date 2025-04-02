package com.alejo.minitimers.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun TimerRing(progress: Float, timeText: String, additionalText: String) {
    val ringColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    // Canvas para dibujar el anillo progresivo
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(300.dp) // Tamaño del anillo
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Anillo de fondo
            drawArc(
                color = Color.Gray,
                startAngle = 270f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(16.dp.toPx(), cap = StrokeCap.Round)
            )

            // Anillo de progreso
            drawArc(
                color = ringColor,
                startAngle = 270f,
                sweepAngle = 360f * progress, // Progreso en base al tiempo restante
                useCenter = false,
                style = Stroke(16.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Texto dentro del anillo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeText,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
            Text(
                text = additionalText,
                style = MaterialTheme.typography.bodySmall,
                color = secondaryTextColor
            )
        }
    }
}