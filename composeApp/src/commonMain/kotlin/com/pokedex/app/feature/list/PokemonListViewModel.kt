package com.pokedex.app.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.core.error.AppError
import com.pokedex.core.result.DataResult
import com.pokedex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import pokedexkmp.composeapp.generated.resources.*

/**
 * List screen ViewModel (MVVM). Only exposes sealed [PokemonListUiState]
 * via StateFlow; the UI (Compose) never directly calls the UseCase
 * nor knows about the Repository.
 */
class PokemonListViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private val pageSize = GetPokemonListUseCase.DEFAULT_PAGE_SIZE
    private var isFetching = false

    init {
        loadFirstPage()
    }

    fun loadFirstPage() {
        currentOffset = 0
        _uiState.value = PokemonListUiState.Loading
        viewModelScope.launch {
            fetchPage(offset = 0, appendToExisting = false)
        }
    }

    /** Called when reaching the end of the list (infinite scroll). */
    fun loadNextPage() {
        val current = _uiState.value
        if (current !is PokemonListUiState.Success || current.endReached || isFetching) return

        _uiState.value = current.copy(isLoadingMore = true)
        viewModelScope.launch {
            fetchPage(offset = currentOffset, appendToExisting = true)
        }
    }

    fun retry() = loadFirstPage()

    private suspend fun fetchPage(offset: Int, appendToExisting: Boolean) {
        isFetching = true
        when (val result = getPokemonListUseCase(limit = pageSize, offset = offset)) {
            is DataResult.Success -> {
                val newItems = result.data
                val previousItems = (_uiState.value as? PokemonListUiState.Success)?.pokemons.orEmpty()
                val combined = if (appendToExisting) previousItems + newItems else newItems

                currentOffset = offset + pageSize

                _uiState.value = when {
                    combined.isEmpty() -> PokemonListUiState.Empty
                    else -> PokemonListUiState.Success(
                        pokemons = combined,
                        isLoadingMore = false,
                        endReached = newItems.size < pageSize,
                    )
                }
            }

            is DataResult.Error -> {
                val previous = _uiState.value
                _uiState.value = if (appendToExisting && previous is PokemonListUiState.Success) {
                    // There was already loaded data: we don't lose the list, just
                    // turn off the "loading more" indicator.
                    previous.copy(isLoadingMore = false)
                } else {
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
                    PokemonListUiState.Error(message)
                }
            }
        }
        isFetching = false
    }
}
