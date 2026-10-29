package com.acs.oboeruapp.features.learningspace.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class LearningSpace(
    val id: String,
    val title: String,
    val icon: DrawableResource? = null
)

