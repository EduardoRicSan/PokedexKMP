package com.pokedex.app.feature.detail

import com.pokedex.domain.model.PokemonDetail

sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState
    data class Success(val detail: PokemonDetail) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}
