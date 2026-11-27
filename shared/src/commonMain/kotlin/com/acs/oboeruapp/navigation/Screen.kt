package com.acs.oboeruapp.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object Onboarding : Screen()

    @Serializable
    data object Landing : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object MyJapanese : Screen()

    @Serializable
    data object Profile : Screen()

    @Serializable
    data class KanaChart(val script: String) : Screen()

    @Serializable
    data class KanaFlashcard(
        val script: String,
        val blockId: String? = null,
    ) : Screen()
}
