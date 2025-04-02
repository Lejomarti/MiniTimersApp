package com.alejo.minitimers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerticalNumberPicker(
    modifier: Modifier = Modifier,
    width: Dp = 45.dp,
    min: Int = 0,
    max: Int = 59,
    selectedValue: Int,
    onValueChange: (Int) -> Unit = {},
    text: String = "time"
) {
    val textColor = MaterialTheme.colorScheme.onBackground
    val buttonColor = MaterialTheme.colorScheme.primary

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text, modifier = Modifier.padding(10.dp), style = MaterialTheme.typography.labelSmall)
        PickerButton(
            size = width,
            icon = Icons.Filled.KeyboardArrowUp,
//            enabled = selectedValue < max,
            buttonColor = buttonColor,
            onClick = {
                val newValue = if (selectedValue >= max) min else selectedValue + 1
                onValueChange(newValue)
            }
        )

        Text(
            text = selectedValue.toString(),
            fontSize = (width.value / 2).sp,
            modifier = Modifier
                .padding(10.dp)
                .width(IntrinsicSize.Max)
                .align(Alignment.CenterHorizontally)
        )

        PickerButton(
            size = width,
            icon = Icons.Filled.KeyboardArrowDown,
//            enabled = selectedValue > min,
            buttonColor = buttonColor,
            onClick = {
                val newValue = if (selectedValue <= min) max else selectedValue - 1
                onValueChange(newValue)
            }
        )
    }
}

@Composable
fun PickerButton(
    size: Dp = 45.dp,
    icon: ImageVector,
    enabled: Boolean = true,
    buttonColor: Color,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(size)
            .clip(CircleShape)
            .background(if (enabled) buttonColor else Color.Gray) // Se adapta al tema
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary // Color del ícono
        )
    }
}

@Preview
@Composable
fun NumberPickerPreview() {
    VerticalNumberPicker(
        selectedValue = 0,
    )
}
