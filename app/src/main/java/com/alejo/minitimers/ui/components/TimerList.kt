package com.alejo.minitimers.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alejo.minitimers.data.PersonalizedTimer

@Composable
fun TimerList(personalizedTimers: List<PersonalizedTimer>, onTimerClick: (Long) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp, 16.dp, 0.dp),// Hacemos que tome solo el espacio necesario
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(personalizedTimers) { timer ->
            TimerCard(
                timeText = timer.personalizedTime,
                color = MaterialTheme.colorScheme.primary,
                onClick = { onTimerClick(timer.personalizedTime) },
//                navController = navController
            )
        }
        item {
            Box(contentAlignment = Alignment.TopCenter) {
            }
        }
    }
}

@Preview
@Composable
fun PreviewTimerList(){
    TimerList(personalizedTimers = listOf(), onTimerClick = {})
}