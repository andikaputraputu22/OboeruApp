package com.acs.oboeruapp

import androidx.compose.ui.window.ComposeUIViewController
import com.acs.oboeruapp.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }
