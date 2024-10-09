package com.alejo.minitimers.ui

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun NumberPickerComponent(
    minValue: Int,
    maxValue: Int,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AndroidView(
            modifier = Modifier, // Puedes agregar más modificaciones como el tamaño
            factory = { context ->
                NumberPicker(context).apply {
                    this.minValue = minValue
                    this.maxValue = maxValue
                    this.value = selectedValue
                    this.wrapSelectorWheel = true // Habilitar el scroll circular
                    setOnValueChangedListener { _, _, newVal ->
                        onValueChange(newVal)
                    }
                }
            },
            update = { picker ->
                // Esto se asegura de que se actualice si cambia el valor externamente
                picker.value = selectedValue
            }
        )
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}