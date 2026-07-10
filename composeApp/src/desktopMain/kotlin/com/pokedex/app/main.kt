package com.pokedex.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pokedex.app.di.initKoin
import com.pokedex.app.di.platformModule

fun main() {
    initKoin(platformModule = platformModule)
    application {
        Window(onCloseRequest = ::exitApplication, title = "Pokédex") {
            App()
        }
    }
}
