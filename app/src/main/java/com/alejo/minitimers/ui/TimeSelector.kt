package com.alejo.minitimers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TimeSelector(
    selectedHour: Int,
    selectedMinute: Int,
    selectedSecond: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onSecondChange: (Int) -> Unit

) {
    val textColor = MaterialTheme.colorScheme.onBackground

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {

            VerticalNumberPicker(
                min = 0,
                max = 24,
                selectedValue = selectedHour,
                onValueChange = onHourChange,
                text = "Hours"
            )
            VerticalNumberPicker(
                min = 0,
                max = 59,
                selectedValue = selectedMinute,
                onValueChange = onMinuteChange,
                text = "Minutes"
            )
            VerticalNumberPicker(
                min = 0,
                max = 59,
                selectedValue = selectedSecond,
                onValueChange = onSecondChange,
                text = "Seconds"
            )
        }
    }
}