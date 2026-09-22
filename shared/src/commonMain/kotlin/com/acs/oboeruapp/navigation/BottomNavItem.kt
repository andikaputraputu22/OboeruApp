package com.acs.oboeruapp.navigation

import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_book
import oboeruapp.shared.generated.resources.ic_chart
import oboeruapp.shared.generated.resources.ic_home
import oboeruapp.shared.generated.resources.ic_more
import org.jetbrains.compose.resources.DrawableResource

data class BottomNavItem(
    val route: Screen,
    val label: String,
    val icon: DrawableResource
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Home", Res.drawable.ic_home),
    BottomNavItem(Screen.MyJapanese, "My Japanese", Res.drawable.ic_book),
    BottomNavItem(Screen.Statistics, "Statistics", Res.drawable.ic_chart),
    BottomNavItem(Screen.More, "More", Res.drawable.ic_more)
)