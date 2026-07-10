package com.pokedex.app.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pokedex.designsystem.components.AppTopBar
import com.pokedex.designsystem.components.EmptyState
import com.pokedex.designsystem.components.ErrorState
import com.pokedex.designsystem.components.PokemonCard
import com.pokedex.designsystem.components.PokemonCardSkeleton
import com.pokedex.designsystem.components.PokemonCardUiModel
import com.pokedex.designsystem.tokens.Spacing
import com.pokedex.domain.model.Pokemon
import org.koin.compose.viewmodel.koinViewModel
import pokedexkmp.composeapp.generated.resources.Res
import pokedexkmp.composeapp.generated.resources.pokemon_list_empty
import org.jetbrains.compose.resources.stringResource

/**
 * List screen. Adaptive to different screen sizes via
 * `LazyVerticalGrid(GridCells.Adaptive)`: in a phone it fits
 * ~2 columns, in a tablet or desktop more, without manual
 * conditional logic per device type.
 */
@Composable
fun PokemonListScreen(
    onPokemonClick: (Int) -> Unit,
    viewModel: PokemonListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar(title = "Pokédex") },
    ) { paddingValues ->
        when (val state = uiState) {
            is PokemonListUiState.Loading -> PokemonGridSkeleton(paddingValues)

            is PokemonListUiState.Success -> PokemonGrid(
                pokemons = state.pokemons,
                isLoadingMore = state.isLoadingMore,
                onPokemonClick = onPokemonClick,
                onLoadMore = viewModel::loadNextPage,
                paddingValues = paddingValues,
            )

            is PokemonListUiState.Empty -> EmptyState(
                message = stringResource(Res.string.pokemon_list_empty),
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            )

            is PokemonListUiState.Error -> ErrorState(
                message = state.message,
                onRetry = viewModel::retry,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            )
        }
    }
}

@Composable
private fun PokemonGridSkeleton(paddingValues: androidx.compose.foundation.layout.PaddingValues) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(Spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        modifier = Modifier.fillMaxSize().padding(paddingValues),
    ) {
        items(8) { PokemonCardSkeleton() }
    }
}

@Composable
private fun PokemonGrid(
    pokemons: List<Pokemon>,
    isLoadingMore: Boolean,
    onPokemonClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    paddingValues: androidx.compose.foundation.layout.PaddingValues,
) {
    val gridState = rememberLazyGridState()

    // Incremental pagination: triggers loadMore when the user
    // approaches the end of the already rendered list.
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= pokemons.size - 4
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(Spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        modifier = Modifier.fillMaxSize().padding(paddingValues),
    ) {
        items(pokemons, key = { it.id }) { pokemon ->
            PokemonCard(
                pokemon = PokemonCardUiModel(
                    id = pokemon.id,
                    displayName = pokemon.name,
                    imageUrl = pokemon.imageUrl,
                ),
                onClick = onPokemonClick,
            )
        }
        if (isLoadingMore) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center,
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }
        }
    }
}
