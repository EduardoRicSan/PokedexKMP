package com.pokedex.app.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.pokedex.designsystem.components.AppTopBar
import com.pokedex.designsystem.components.DetailSkeleton
import com.pokedex.designsystem.components.ErrorState
import com.pokedex.designsystem.components.StatBar
import com.pokedex.designsystem.components.TypeChip
import com.pokedex.designsystem.tokens.Radius
import com.pokedex.designsystem.tokens.Sizing
import com.pokedex.designsystem.tokens.Spacing
import com.pokedex.domain.model.PokemonDetail
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pokedexkmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = koinViewModel(parameters = { parametersOf(pokemonId) }),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppTopBar(
                title = (uiState as? PokemonDetailUiState.Success)
                    ?.detail?.name?.replaceFirstChar { it.uppercase() }
                    ?: stringResource(Res.string.detail_title),
                onBackClick = onBackClick,
            )
        },
    ) { paddingValues ->
        when (val state = uiState) {
            is PokemonDetailUiState.Loading -> DetailSkeleton(
                modifier = Modifier.padding(paddingValues),
            )

            is PokemonDetailUiState.Success -> PokemonDetailContent(
                detail = state.detail,
                modifier = Modifier.padding(paddingValues),
            )

            is PokemonDetailUiState.Error -> ErrorState(
                message = state.message,
                onRetry = viewModel::retry,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        }
    }
}

@Composable
private fun PokemonDetailContent(detail: PokemonDetail, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                AsyncImage(
                    model = detail.imageUrl,
                    contentDescription = stringResource(Res.string.detail_image_description, detail.name),
                    modifier = Modifier
                        .size(Sizing.detailImageSize)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(Radius.lg),
                        ),
                )
                Text(
                    text = "#${detail.id} ${detail.name.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    detail.types.forEach { type -> TypeChip(typeName = type) }
                }
            }
        }

        item { SectionTitle(stringResource(Res.string.section_general_info)) }
        item {
            InfoRow(label = stringResource(Res.string.label_height), value = "${detail.heightMeters} m")
        }
        item {
            InfoRow(label = stringResource(Res.string.label_weight), value = "${detail.weightKg} kg")
        }
        item {
            InfoRow(label = stringResource(Res.string.label_base_experience), value = detail.baseExperience.toString())
        }

        if (detail.abilities.isNotEmpty()) {
            item { SectionTitle(stringResource(Res.string.section_abilities)) }
            item {
                Text(
                    text = detail.abilities.joinToString(", ") { it.replaceFirstChar { c -> c.uppercase() } },
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        if (detail.stats.isNotEmpty()) {
            item { SectionTitle(stringResource(Res.string.section_stats)) }
            items(detail.stats) { stat ->
                StatBar(label = stat.name, value = stat.baseValue)
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
