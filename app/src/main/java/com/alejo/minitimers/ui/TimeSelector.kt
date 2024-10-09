package com.alejo.minitimers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            NumberPickerComponent(
                minValue = 0,
                maxValue = 99,
                selectedValue = selectedHour,
                onValueChange = onHourChange,
                text = "Hours"
            )
            NumberPickerComponent(
                minValue = 0,
                maxValue = 59,
                selectedValue = selectedMinute,
                onValueChange = onMinuteChange,
                text = "Min"
            )
            NumberPickerComponent(
                minValue = 0,
                maxValue = 59,
                selectedValue = selectedSecond,
                onValueChange = onSecondChange,
                text = "Sec"
            )


        }
    }
}