package com.alejo.minitimers.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun TimerRing(
    progress: Float,
    timeText: String,
    additionalText: String,
    onLongPress: (() -> Unit)? = null
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val timerRingHeight = screenHeight * 0.35f


        val ringColor = MaterialTheme.colorScheme.primary
        val textColor = MaterialTheme.colorScheme.onBackground
        val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(timerRingHeight)
                .aspectRatio(1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            onLongPress?.invoke()
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.Gray,
                    startAngle = 270f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(16.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = ringColor,
                    startAngle = 270f,
                    sweepAngle = 360f * progress, // Progreso en base al tiempo restante
                    useCenter = false,
                    style = Stroke(16.dp.toPx(), cap = StrokeCap.Round)
                )
            }

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