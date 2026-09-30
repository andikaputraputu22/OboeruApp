package com.acs.oboeruapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.acs.oboeruapp.navigation.Screen
import com.acs.oboeruapp.ui.components.AppBottomNavigationBar
import com.acs.oboeruapp.ui.screens.home.HomeScreen
import com.acs.oboeruapp.ui.screens.learningSpace.ChooseTemplateScreen
import com.acs.oboeruapp.ui.theme.OboeruTheme

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
                Screen.MyJapanese::class,
                Screen.Statistics::class,
                Screen.More::class
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
                        AppBottomNavigationBar(navController)
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Screen.Home> { HomeScreen(
                        onAddLearningSpaceClick = {
                            navController.navigate(Screen.ChooseLearningSpaceTemplate)
                        }
                    ) }
                    composable<Screen.MyJapanese> {  }
                    composable<Screen.Statistics> {  }
                    composable<Screen.More> {  }
                    composable<Screen.ChooseLearningSpaceTemplate> {
                        ChooseTemplateScreen(
                            onBackClick = { navController.popBackStack() },
                            onNextClick = { templateId -> },
                            onCreateFromScratchClick = {}
                        )
                    }
                }
            }
        }
    }
}