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
}
