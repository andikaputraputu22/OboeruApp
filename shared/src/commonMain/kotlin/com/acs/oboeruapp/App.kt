package com.acs.oboeruapp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.acs.oboeruapp.core.designsystem.theme.OboeruTheme
import com.acs.oboeruapp.features.auth.domain.model.AuthState
import com.acs.oboeruapp.features.home.ui.HomeScreen
import com.acs.oboeruapp.features.onboarding.ui.LandingScreen
import com.acs.oboeruapp.features.onboarding.ui.OnboardingScreen
import com.acs.oboeruapp.features.profile.ui.ProfileScreen
import com.acs.oboeruapp.navigation.Screen
import com.acs.oboeruapp.navigation.components.AppBottomNavigationBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    onAppReady: () -> Unit = {},
) {
    OboeruTheme {
        val appViewModel: AppViewModel = koinViewModel()
        val appUiState by appViewModel.uiState.collectAsStateWithLifecycle()
        val startDestination = appUiState.initialDestination

        LaunchedEffect(startDestination != null) {
            if (startDestination != null) {
                onAppReady()
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (startDestination != null) {
                OboeruNavContent(
                    startDestination = startDestination,
                    authState = appUiState.authState,
                    onCompleteOnboarding = appViewModel::completeOnboarding,
                )
            }
        }
    }
}

@Composable
private fun OboeruNavContent(
    startDestination: Screen,
    authState: AuthState,
    onCompleteOnboarding: () -> Unit,
) {
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

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            val destination = navController.currentDestination ?: return@LaunchedEffect
            val isOnAuthRoute = destination.hierarchy.any {
                it.hasRoute(Screen.Onboarding::class) || it.hasRoute(Screen.Landing::class)
            }
            if (!isOnAuthRoute) {
                navController.navigate(Screen.Landing) {
                    popUpTo(navController.graph.id) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

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
            startDestination = startDestination,
            modifier = Modifier.padding(
                start = innerPadding.calculateStartPadding(layoutDirection),
                top = 0.dp,
                end = innerPadding.calculateEndPadding(layoutDirection),
                bottom = 0.dp,
            )
        ) {
            composable<Screen.Onboarding>(
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(300))
                }
            ) {
                OnboardingScreen(
                    topBarPadding = innerPadding.calculateTopPadding(),
                    bottomBarPadding = innerPadding.calculateBottomPadding(),
                    onFinishOnboarding = {
                        onCompleteOnboarding()
                        navController.navigate(Screen.Landing) {
                            popUpTo(Screen.Onboarding) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Screen.Landing>(
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(300))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(300))
                }
            ) {
                val navigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                LandingScreen(
                    topBarPadding = innerPadding.calculateTopPadding(),
                    bottomBarPadding = innerPadding.calculateBottomPadding(),
                    onAppleSignInClick = navigateToHome,
                    onLoginSuccess = navigateToHome,
                )
            }

            composable<Screen.Home>(
                exitTransition = {
                    if (targetState.destination.hasRoute(Screen.Profile::class)) {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(350, easing = FastOutSlowInEasing),
                            targetOffset = { it / 3 }
                        ) + fadeOut(animationSpec = tween(300))
                    } else null
                },
                popEnterTransition = {
                    if (initialState.destination.hasRoute(Screen.Profile::class)) {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(350, easing = FastOutSlowInEasing),
                            initialOffset = { it / 3 }
                        ) + fadeIn(animationSpec = tween(300))
                    } else null
                }
            ) {
                HomeScreen(
                    topBarPadding = innerPadding.calculateTopPadding(),
                    bottomBarPadding = innerPadding.calculateBottomPadding(),
                    onProfileClick = {
                        navController.navigate(Screen.Profile)
                    }
                )
            }

            composable<Screen.MyJapanese> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = innerPadding.calculateTopPadding(),
                            bottom = innerPadding.calculateBottomPadding(),
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "My Japanese",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            composable<Screen.Profile>(
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(300))
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(300))
                }
            ) {
                ProfileScreen(
                    topBarPadding = innerPadding.calculateTopPadding(),
                    bottomBarPadding = innerPadding.calculateBottomPadding(),
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
