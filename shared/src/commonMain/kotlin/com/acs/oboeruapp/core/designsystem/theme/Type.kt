package com.acs.oboeruapp.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.inter_bold
import oboeruapp.shared.generated.resources.inter_medium
import oboeruapp.shared.generated.resources.inter_regular
import oboeruapp.shared.generated.resources.inter_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun oboeruTypography(): Typography {
    val inter = FontFamily(
        Font(Res.font.inter_regular, FontWeight.Normal),
        Font(Res.font.inter_medium, FontWeight.Medium),
        Font(Res.font.inter_semibold, FontWeight.SemiBold),
        Font(Res.font.inter_bold, FontWeight.Bold)
    )

    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontFamily = inter),
        displayMedium = base.displayMedium.copy(fontFamily = inter),
        displaySmall = base.displaySmall.copy(fontFamily = inter),
        headlineLarge = base.headlineLarge.copy(fontFamily = inter, fontWeight = FontWeight.SemiBold),
        headlineMedium = base.headlineMedium.copy(fontFamily = inter, fontWeight = FontWeight.SemiBold),
        headlineSmall = base.headlineSmall.copy(fontFamily = inter, fontWeight = FontWeight.SemiBold),
        titleLarge = base.titleLarge.copy(fontFamily = inter),
        titleMedium = base.titleMedium.copy(fontFamily = inter, fontWeight = FontWeight.Medium),
        titleSmall = base.titleSmall.copy(fontFamily = inter, fontWeight = FontWeight.Medium),
        bodyLarge = base.bodyLarge.copy(fontFamily = inter),
        bodyMedium = base.bodyMedium.copy(fontFamily = inter),
        bodySmall = base.bodySmall.copy(fontFamily = inter),
        labelLarge = base.labelLarge.copy(fontFamily = inter, fontWeight = FontWeight.Medium),
        labelMedium = base.labelMedium.copy(fontFamily = inter, fontWeight = FontWeight.Medium),
        labelSmall = base.labelSmall.copy(fontFamily = inter, fontWeight = FontWeight.Medium),
    )
}

