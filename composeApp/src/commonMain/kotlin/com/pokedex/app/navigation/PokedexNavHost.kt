package com.pokedex.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pokedex.app.feature.detail.PokemonDetailScreen
import com.pokedex.app.feature.list.PokemonListScreen

@Composable
fun PokedexNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Destination.PokemonList,
    ) {
        composable<Destination.PokemonList> {
            PokemonListScreen(
                onPokemonClick = { id ->
                    navController.navigate(Destination.PokemonDetail(pokemonId = id))
                },
            )
        }
        composable<Destination.PokemonDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Destination.PokemonDetail>()
            PokemonDetailScreen(
                pokemonId = args.pokemonId,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
