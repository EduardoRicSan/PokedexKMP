package com.pokedex.app.feature.list

import com.pokedex.domain.model.Pokemon

/**
 * Estado sellado de la pantalla de listado. Al ser sellado, el `when`
 * en el Composable es exhaustivo: si se agrega un nuevo estado, el
 * compilador obliga a manejarlo en la UI.
 */
sealed interface PokemonListUiState {
    /** Primera carga, sin datos previos: se muestra el skeleton completo. */
    data object Loading : PokemonListUiState

    data class Success(
        val pokemons: List<Pokemon>,
        val isLoadingMore: Boolean = false,
        val endReached: Boolean = false,
    ) : PokemonListUiState

    data object Empty : PokemonListUiState

    data class Error(val message: String) : PokemonListUiState
}
