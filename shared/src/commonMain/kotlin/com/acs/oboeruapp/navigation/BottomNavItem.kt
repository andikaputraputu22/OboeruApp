package com.acs.oboeruapp.navigation

import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_book
import oboeruapp.shared.generated.resources.ic_home
import org.jetbrains.compose.resources.DrawableResource

data class BottomNavItem(
    val route: Screen,
    val label: String,
    val icon: DrawableResource
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Home", Res.drawable.ic_home),
    BottomNavItem(Screen.MyJapanese, "My Japanese", Res.drawable.ic_book),
)
