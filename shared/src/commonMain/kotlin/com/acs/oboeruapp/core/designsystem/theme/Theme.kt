package com.acs.oboeruapp.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OboeruColorScheme = lightColorScheme(
    primary = OboeruPrimary,
    onPrimary = Color.White,
    primaryContainer = OboeruPrimaryContainer,
    onPrimaryContainer = OboeruOnPrimaryContainer,

    secondary = OboeruSecondary,
    onSecondary = Color.White,
    secondaryContainer = OboeruSecondaryContainer,
    onSecondaryContainer = OboeruNeutral,

    tertiary = OboeruTertiary,
    onTertiary = Color.White,
    tertiaryContainer = OboeruTertiaryContainer,
    onTertiaryContainer = OboeruTertiary,

    background = OboeruBackground,
    onBackground = OboeruNeutral,
    surface = OboeruSurface,
    onSurface = OboeruNeutral,
    surfaceVariant = OboeruSecondaryContainer,
    onSurfaceVariant = OboeruSecondary,

    inverseSurface = OboeruNeutral,
    inverseOnSurface = Color.White,

    outline = OboeruOutline,
    outlineVariant = OboeruOutlineVariant,
    error = OboeruError,
    onError = Color.White,
)

@Composable
fun OboeruTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OboeruColorScheme,
        typography = oboeruTypography(),
        content = content
    )
}

