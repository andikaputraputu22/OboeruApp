package com.acs.oboeruapp.features.profile.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProfileItemDivider(
    modifier: Modifier = Modifier,
    startIndent: Dp = 54.dp,
) {
    HorizontalDivider(
        modifier = modifier.padding(start = startIndent),
        thickness = 0.6.dp,
        color = Color.White.copy(alpha = 0.06f),
    )
}