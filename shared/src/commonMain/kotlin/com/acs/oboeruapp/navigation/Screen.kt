package com.acs.oboeruapp.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object Home : Screen()

    @Serializable
    data object MyJapanese : Screen()
}
