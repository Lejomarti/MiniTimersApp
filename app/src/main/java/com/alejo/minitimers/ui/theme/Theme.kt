package com.alejo.minitimers.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)


@Composable
fun MiniTimersTheme(
     darkTheme: Boolean = isSystemInDarkTheme(),
     // Dynamic color is available on Android 12+
     dynamicColor: Boolean = false,
     themeColorName: String = "Blue",
     content: @Composable () -> Unit
 ) {
    val selectedColors = themeColors[themeColorName] ?: themeColors["Blue"]!! // Valor por defecto

    fun blendColors(color1: Color, color2: Color, ratio: Float): Color {
        val blendedColor = ColorUtils.blendARGB(color1.toArgb(), color2.toArgb(), ratio)
        return Color(blendedColor)
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = selectedColors[0],
            onPrimary = selectedColors[1],
            secondary = selectedColors[2],
            onSecondary = selectedColors[3],
            tertiary = selectedColors[4],
            onTertiary = selectedColors[5],
            surface = selectedColors[6],
            onSurface = blendColors(Color(0xFF131313), selectedColors[7], 0.15f),
            background = blendColors(Color(0xFF131313), selectedColors[6], 0.15f),
            surfaceVariant = selectedColors[9],
//            onSurfaceVariant = selectedColors[10]

        )
    } else {
        lightColorScheme(
            primary = selectedColors[0],
            onPrimary = selectedColors[1],
            secondary = selectedColors[2],
            onSecondary = selectedColors[3],
            tertiary = selectedColors[4],
            onTertiary = selectedColors[5],
            surface = selectedColors[6], // Fondo de superficies
            onSurface = blendColors(Color(0xFF131313), selectedColors[7], 0.15f),
            background = selectedColors[8], // Fondo general
            surfaceVariant = selectedColors[9],
//            onSurfaceVariant = selectedColors[10]
        )
    }


     MaterialTheme(
         colorScheme = colorScheme,
         typography = Typography,
         content = content
     )
 }
