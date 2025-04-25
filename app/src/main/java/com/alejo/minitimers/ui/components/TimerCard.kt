package com.alejo.minitimers.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavController
import com.alejo.minitimers.navigation.AppScreens
import com.alejo.minitimers.ui.screens.formatTime

@Composable
fun TimerCard(timeText: Long, color: Color, onClick: (() -> Unit)? = null) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(0.77f)
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
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(10.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = color,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(18.dp.toPx())
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
fun PlusIcon(
    enabled: Boolean,
    navController: NavController?
) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(0.77f),
        color = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(10.dp)
            ) {
                if (!enabled) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Add Timer",
                            tint = Color.White
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            navController?.navigate(route = AppScreens.AddTimerScreen.route)
                        },
                        modifier = Modifier
                            .fillMaxSize()
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
            Spacer(Modifier.height(50.dp))
        }
    }
}


@Preview
@Composable
fun PreviewTimerCard() {
    PlusIcon(
        enabled = true,
        navController = null
    )
}