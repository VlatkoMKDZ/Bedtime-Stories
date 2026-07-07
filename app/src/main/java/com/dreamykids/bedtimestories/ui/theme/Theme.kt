package com.dreamykids.bedtimestories.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CreamBackground = Color(0xFFFFF8F0)
val CreamSurface = Color(0xFFFFFFFF)
val SoftOrange = Color(0xFFFFB879)
val DarkerOrange = Color(0xFFFF9966)
val SoftMint = Color(0xFFA7D7C5)
val DarkBrown = Color(0xFF3D2C20)
val MediumBrown = Color(0xFF8D7766)
val SuccessGreen = Color(0xFF7EC89A)
val WarmBorder = Color(0xFFF1E6D8)

val Night = CreamBackground
val Card = CreamSurface
val Glow = SoftOrange

@Composable
fun DreamyTheme(darkMode: Boolean = false, content: @Composable () -> Unit) {
    val lightColors = lightColorScheme(
        primary = SoftOrange,
        onPrimary = DarkBrown,
        primaryContainer = DarkerOrange,
        onPrimaryContainer = CreamSurface,
        secondary = SoftMint,
        onSecondary = DarkBrown,
        background = CreamBackground,
        onBackground = DarkBrown,
        surface = CreamSurface,
        onSurface = DarkBrown,
        surfaceVariant = WarmBorder,
        onSurfaceVariant = MediumBrown,
        outline = WarmBorder,
        tertiary = SuccessGreen
    )
    val darkColors = darkColorScheme(
        primary = SoftOrange,
        onPrimary = DarkBrown,
        primaryContainer = DarkerOrange,
        onPrimaryContainer = CreamSurface,
        secondary = SoftMint,
        onSecondary = DarkBrown,
        background = DarkBrown,
        onBackground = CreamBackground,
        surface = Color(0xFF4A3527),
        onSurface = CreamBackground,
        surfaceVariant = MediumBrown,
        onSurfaceVariant = WarmBorder,
        outline = MediumBrown,
        tertiary = SuccessGreen
    )

    MaterialTheme(
        colorScheme = if (darkMode) darkColors else lightColors,
        typography = Typography(),
        content = content
    )
}
