package com.acs.oboeruapp.data.model

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource

data class LearningSpace(
    val id: String,
    val title: String,
    val icon: DrawableResource? = null
)

data class IconOption(
    val id: String,
    val icon: DrawableResource
)

data class ColorOption(
    val id: String,
    val color: Color
)