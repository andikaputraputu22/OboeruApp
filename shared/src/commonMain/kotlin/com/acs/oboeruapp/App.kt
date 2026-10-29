package com.acs.oboeruapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.features.home.ui.HomeScreen
import com.acs.oboeruapp.features.learningspace.ui.screens.ChooseTemplateScreen
import com.acs.oboeruapp.features.learningspace.ui.screens.CustomizeLearningSpaceScreen
import com.acs.oboeruapp.features.learningspace.ui.screens.LearningSpaceCreatedScreen
import com.acs.oboeruapp.features.learningspace.ui.screens.ReviewLearningSpaceScreen
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
                    composable<Screen.Home> {
                        HomeScreen(
                            onAddLearningSpaceClick = {
                                navController.navigate(Screen.ChooseLearningSpaceTemplate)
                            }
                        )
                    }
                    composable<Screen.MyJapanese> { }
                    composable<Screen.Statistics> { }
                    composable<Screen.More> { }
                    composable<Screen.ChooseLearningSpaceTemplate> {
                        ChooseTemplateScreen(
                            onBackClick = { navController.popBackStack() },
                            onNextClick = { templateId -> },
                            onCreateFromScratchClick = {
                                navController.navigate(Screen.CustomizeLearningSpace)
                            }
                        )
                    }
                    composable<Screen.CustomizeLearningSpace> {
                        CustomizeLearningSpaceScreen(
                            onBackClick = { navController.popBackStack() },
                            onNextClick = { name, iconId, colorId, description ->
                                navController.navigate(Screen.ReviewLearningSpace)
                            }
                        )
                    }
                    composable<Screen.ReviewLearningSpace> {
                        ReviewLearningSpaceScreen(
                            onCreateClick = {
                                navController.navigate(Screen.LearningSpaceCreated) {
                                    popUpTo(Screen.ChooseLearningSpaceTemplate) { inclusive = true }
                                }
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                    composable<Screen.LearningSpaceCreated> {
                        LearningSpaceCreatedScreen(
                            onGoToHomeClick = {
                                navController.navigate(Screen.Home) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onCreateAnotherClick = {
                                navController.navigate(Screen.ChooseLearningSpaceTemplate) {
                                    popUpTo(Screen.LearningSpaceCreated) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
