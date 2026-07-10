package com.pokedex.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PokedexColors.PokedexRed,
    onPrimary = PokedexColors.SurfaceLight,
    secondary = PokedexColors.PokedexBlue,
    background = PokedexColors.BackgroundLight,
    surface = PokedexColors.SurfaceLight,
    onBackground = PokedexColors.OnSurfaceLight,
    onSurface = PokedexColors.OnSurfaceLight,
    error = PokedexColors.ErrorRed,
)

private val DarkColors = darkColorScheme(
    primary = PokedexColors.PokedexRed,
    onPrimary = PokedexColors.SurfaceDark,
    secondary = PokedexColors.PokedexYellow,
    background = PokedexColors.BackgroundDark,
    surface = PokedexColors.SurfaceDark,
    onBackground = PokedexColors.OnSurfaceDark,
    onSurface = PokedexColors.OnSurfaceDark,
    error = PokedexColors.ErrorRed,
)

@Composable
fun PokedexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = PokedexTypography,
        content = content,
    )
}
