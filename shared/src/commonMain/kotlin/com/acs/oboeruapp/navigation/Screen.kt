package com.acs.oboeruapp.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object Home: Screen()

    @Serializable
    data object MyJapanese: Screen()

    @Serializable
    data object Statistics: Screen()

    @Serializable
    data object More: Screen()

    @Serializable
    data object ChooseLearningSpaceTemplate: Screen()

    @Serializable
    data object CustomizeLearningSpace: Screen()

    @Serializable
    data object ReviewLearningSpace: Screen()
}