package com.acs.oboeruapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.features.home.ui.HomeScreen
import com.acs.oboeruapp.navigation.Screen
import com.acs.oboeruapp.navigation.components.AppBottomNavigationBar

@Composable
@Preview
fun App() {
    OboeruTheme {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination

        val topLevelRoutes = remember {
            setOf(
                Screen.Home::class,
                Screen.MyJapanese::class
            )
        }

        val showBottomNav = currentRoute?.hierarchy?.any { destination ->
            topLevelRoutes.any { destination.hasRoute(it) }
        } == true

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = {
                    if (showBottomNav) {
                        AppBottomNavigationBar(
                            navController = navController,
                            onAddClick = {
                                // TODO: Handle add action
                            }
                        )
                    }
                }
            ) { innerPadding ->
                val layoutDirection = LocalLayoutDirection.current
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home,
                    modifier = Modifier.padding(
                        start = innerPadding.calculateStartPadding(layoutDirection),
                        top = innerPadding.calculateTopPadding(),
                        end = innerPadding.calculateEndPadding(layoutDirection),
                        bottom = if (showBottomNav) 0.dp else innerPadding.calculateBottomPadding(),
                    )
                ) {
                    composable<Screen.Home> {
                        HomeScreen(
                            bottomBarPadding = innerPadding.calculateBottomPadding()
                        )
                    }
                    composable<Screen.MyJapanese> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "My Japanese",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
