package com.alejo.minitimers.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerRing(
    progress: Float,
    timeText: String,
    additionalText: String,
    onLongPress: (() -> Unit)? = null,
    repeatCount: Int = 0,
    onRepeatClick: ()-> Unit
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

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(0.dp)
            ){
                IconButton(onClick = onRepeatClick) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Repeat",
                        tint = if (repeatCount > 0) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                    if (repeatCount > 0) {
                        Text(
                            text = repeatCount.toString(),
                            color = textColor,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-4).dp, y = 4.dp)
                        )
                    }
                }
            }
        }
    }


@Preview
@Composable
fun TimerRingPreview(){
    TimerRing(
        progress = 1.0F,
        timeText = "00:00",
        additionalText = "00:00",
        onLongPress = {},
        repeatCount = 1
    ) { }
}