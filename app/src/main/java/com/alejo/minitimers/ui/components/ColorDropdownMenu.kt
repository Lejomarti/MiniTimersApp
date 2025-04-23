package com.alejo.minitimers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorDropdownMenu(
    themeColors: Map<String, List<Color>>,
    selectedThemeColor: String,
    onColorSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                   .fillMaxWidth()
                   .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Theme color", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { expanded = true }) {
                Text(text = selectedThemeColor, style = MaterialTheme.typography.titleMedium)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            themeColors.keys.forEach { colorName ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(themeColors[colorName]?.get(0) ?: Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(colorName, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                        }
                    },
                    onClick = {
                        onColorSelected(colorName)
                        expanded = false
                    }
                )
            }
        }
    }
}