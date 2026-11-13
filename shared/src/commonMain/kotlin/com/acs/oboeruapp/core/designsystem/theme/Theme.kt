package com.acs.oboeruapp.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val OboeruDarkColorScheme = darkColorScheme(
    primary = OboeruDarkPrimary,
    onPrimary = OboeruDarkOnPrimary,
    primaryContainer = OboeruDarkPrimaryContainer,
    onPrimaryContainer = OboeruDarkOnPrimaryContainer,

    secondary = OboeruDarkSecondary,
    onSecondary = OboeruDarkOnSecondary,
    secondaryContainer = OboeruDarkSecondaryContainer,
    onSecondaryContainer = OboeruDarkOnSecondaryContainer,

    tertiary = OboeruDarkTertiary,
    onTertiary = OboeruDarkOnTertiary,
    tertiaryContainer = OboeruDarkTertiaryContainer,
    onTertiaryContainer = OboeruDarkOnTertiaryContainer,

    background = OboeruDarkBackground,
    onBackground = OboeruDarkNeutral,
    surface = OboeruDarkSurface,
    onSurface = OboeruDarkNeutral,
    surfaceVariant = OboeruDarkSurfaceVariant,
    onSurfaceVariant = OboeruDarkOnSurfaceVariant,

    inverseSurface = OboeruDarkInverseSurface,
    inverseOnSurface = OboeruDarkInverseOnSurface,

    outline = OboeruDarkOutline,
    outlineVariant = OboeruDarkOutlineVariant,
    error = OboeruDarkError,
    onError = OboeruDarkOnError,
)

val OboeruLightColorScheme = lightColorScheme(
    primary = OboeruLightPrimary,
    onPrimary = Color.White,
    primaryContainer = OboeruLightPrimaryContainer,
    onPrimaryContainer = OboeruLightOnPrimaryContainer,

    secondary = OboeruLightSecondary,
    onSecondary = Color.White,
    secondaryContainer = OboeruLightSecondaryContainer,
    onSecondaryContainer = OboeruLightNeutral,

    tertiary = OboeruLightTertiary,
    onTertiary = Color.White,
    tertiaryContainer = OboeruLightTertiaryContainer,
    onTertiaryContainer = OboeruLightTertiary,

    background = OboeruLightBackground,
    onBackground = OboeruLightNeutral,
    surface = OboeruLightSurface,
    onSurface = OboeruLightNeutral,
    surfaceVariant = OboeruLightSecondaryContainer,
    onSurfaceVariant = OboeruLightSecondary,

    inverseSurface = OboeruLightNeutral,
    inverseOnSurface = Color.White,

    outline = OboeruLightOutline,
    outlineVariant = OboeruLightOutlineVariant,
    error = OboeruLightError,
    onError = Color.White,
)

@Composable
fun OboeruTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) OboeruDarkColorScheme else OboeruLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = oboeruTypography(),
        content = content
    )
}
