package com.alejo.minitimers.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta de colores naranja
val OrangePrimary = Color(0xFFFF9800)
val OrangeOnPrimary = Color(0xFFFFFFFF)
val OrangeSecondary = Color(0xFFFFC107)
val OrangeOnSecondary = Color(0xFF000000)

// Paleta de colores rojo
val RedPrimary = Color(0xFFD32F2F)
val RedOnPrimary = Color(0xFFFFFFFF)
val RedSecondary = Color(0xFFFF5252)
val RedOnSecondary = Color(0xFF000000)

// Paleta de colores azul
val BluePrimary = Color(0xFF1976D2)
val BlueOnPrimary = Color(0xFFFFFFFF)
val BlueSecondary = Color(0xFF64B5F6)
val BlueOnSecondary = Color(0xFF000000)

// Paleta de colores verde
val GreenPrimary = Color(0xFF388E3C)
val GreenOnPrimary = Color(0xFFFFFFFF)
val GreenSecondary = Color(0xFF81C784)
val GreenOnSecondary = Color(0xFF000000)

// Paleta de colores amarillo
val YellowPrimary = Color(0xFFFFEB3B)
val YellowOnPrimary = Color(0xFF000000)
val YellowSecondary = Color(0xFFFFF176)
val YellowOnSecondary = Color(0xFF000000)

// Lista de temas
val themeColors = mapOf(
    "Orange" to listOf(OrangePrimary, OrangeOnPrimary, OrangeSecondary, OrangeOnSecondary),
    "Red" to listOf(RedPrimary, RedOnPrimary, RedSecondary, RedOnSecondary),
    "Blue" to listOf(BluePrimary, BlueOnPrimary, BlueSecondary, BlueOnSecondary),
    "Green" to listOf(GreenPrimary, GreenOnPrimary, GreenSecondary, GreenOnSecondary),
    "Yellow" to listOf(YellowPrimary, YellowOnPrimary, YellowSecondary, YellowOnSecondary)
)