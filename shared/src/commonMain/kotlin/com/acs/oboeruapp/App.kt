package com.acs.oboeruapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acs.oboeruapp.navigation.Screen
import com.acs.oboeruapp.ui.components.AppBottomNavigationBar
import com.acs.oboeruapp.ui.screens.home.HomeScreen
import com.acs.oboeruapp.ui.theme.OboeruTheme

@Composable
@Preview
fun App() {
    OboeruTheme {
        val navController = rememberNavController()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = {
                    AppBottomNavigationBar(navController)
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Screen.Home> { HomeScreen() }
                    composable<Screen.MyJapanese> { HomeScreen() }
                    composable<Screen.Statistics> { HomeScreen() }
                    composable<Screen.More> { HomeScreen() }
                }
            }
        }
    }
}