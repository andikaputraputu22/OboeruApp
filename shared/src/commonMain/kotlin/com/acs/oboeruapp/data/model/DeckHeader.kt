package com.acs.oboeruapp.data.model

import org.jetbrains.compose.resources.DrawableResource

data class DeckHeader(
    val title: String,
    val description: String,
    val backgroundImageUrl: String
)

sealed interface DeckBlock {
    val id: String
    val icon: DrawableResource
    val title: String

    data class ProgressBlock(
        override val id: String,
        override val icon: DrawableResource,
        override val title: String,
        val level: String,      // "N5"
        val current: Int,       // 6
        val target: Int,        // 20
    ) : DeckBlock

    data class SimpleBlock(
        override val id: String,
        override val icon: DrawableResource,
        override val title: String,
        val subtitle: String,   // "18 items due" / "25 minutes"
    ) : DeckBlock
}