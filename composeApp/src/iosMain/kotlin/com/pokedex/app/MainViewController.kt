package com.pokedex.app

import androidx.compose.ui.window.ComposeUIViewController
import com.pokedex.app.di.initKoin
import com.pokedex.app.di.platformModule
import platform.UIKit.UIViewController

private var koinStarted = false

fun MainViewController(): UIViewController {
    if (!koinStarted) {
        initKoin(platformModule = platformModule)
        koinStarted = true
    }
    return ComposeUIViewController { App() }
}
