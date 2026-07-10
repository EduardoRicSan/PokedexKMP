package com.pokedex.app.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.core.error.AppError
import com.pokedex.core.result.DataResult
import com.pokedex.domain.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import pokedexkmp.composeapp.generated.resources.*

/**
 * Detail ViewModel. Receives only the `pokemonId` (primitive navigation
 * parameter) and requests its own data via UseCase — it doesn't depend
 * on the list ViewModel nor receives a pre-built Pokemon object.
 */
class PokemonDetailViewModel(
    private val pokemonId: Int,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        _uiState.value = PokemonDetailUiState.Loading
        viewModelScope.launch {
            when (val result = getPokemonDetailUseCase(pokemonId)) {
                is DataResult.Success -> _uiState.value = PokemonDetailUiState.Success(result.data)
                is DataResult.Error -> {
                    val error = result.error
                    val message = when (error) {
                        is AppError.NoConnection -> getString(Res.string.error_no_connection)
                        is AppError.Timeout -> getString(Res.string.error_timeout)
                        is AppError.NotFound -> getString(Res.string.error_not_found)
                        is AppError.ServerError -> getString(Res.string.error_server_problem, error.code)
                        is AppError.SerializationFailure -> getString(Res.string.error_serialization)
                        is AppError.LocalStorageError -> getString(Res.string.error_local_storage)
                        is AppError.Unknown -> getString(Res.string.error_unknown)
                    }
                    _uiState.value = PokemonDetailUiState.Error(message)
                }
            }
        }
    }
}
