package com.acs.oboeruapp.features.learningspace.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class LearningSpaceTemplate(
    val id: String,
    val title: String,
    val description: String,
    val icon: DrawableResource
)

