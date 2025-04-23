
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun TimersCarousel(
    timers: List<Pair<String, Long>>,
    color: Color,
    enabled: Boolean,
    navController: NavController?,
    onClick: (String) -> Unit
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

        items(timers) { (id,time) ->
            TimerCard(timeText = time, color = color, onClick = { onClick(id) })
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

