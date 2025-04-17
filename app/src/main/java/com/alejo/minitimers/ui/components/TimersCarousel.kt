
package com.alejo.minitimers.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun TimersCarousel(
    timers: List<Long>,
    color: Color,
    enabled: Boolean,
    navController: NavController?,
    onClick: (Long) -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val carouselHeight = screenHeight * 0.15f

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(carouselHeight)
    ) {
        item {
            Box(
                modifier = Modifier
                    .width(0.dp)
                    .height(60.dp)
            )
        }

        items(timers) { time ->
            TimerCard(timeText = time, color = color, onClick = { onClick(time) })
        }
        item {
            PlusIcon(enabled, navController)
        }
        item {
            Box(
                modifier = Modifier
                    .width(0.dp)
                    .height(60.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MinitimersCarouselPreview() {
    val timersList =listOf(
        5_000L,
        15_000L,
        30_000L,
        60_000L,
        120_000L,
        300_000L,
    )

    TimersCarousel(
            timers = timersList,
            color = Color.Red,
            enabled = false,
            navController = null,
            onClick = { }
        )

}

