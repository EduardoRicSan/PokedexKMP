package com.pokedex.app

import androidx.compose.runtime.Composable
import com.pokedex.app.navigation.PokedexNavHost
import com.pokedex.designsystem.theme.PokedexTheme

/** Composable raíz, compartido por Android/iOS/Desktop. */
@Composable
fun App() {
    PokedexTheme {
        PokedexNavHost()
    }
}
