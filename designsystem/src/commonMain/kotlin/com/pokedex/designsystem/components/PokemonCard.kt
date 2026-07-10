package com.pokedex.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pokedex.designsystem.tokens.Radius
import com.pokedex.designsystem.tokens.Sizing
import com.pokedex.designsystem.tokens.Spacing
import org.jetbrains.compose.resources.stringResource
import com.pokedex.designsystem.Res
import com.pokedex.designsystem.card_view_detail

/**
 * Simple UI model, independent of the domain model. The design
 * system never receives `domain` entities directly: it receives primitives
 * and presentation models, to be reusable without coupling to
 * any specific feature.
 */
data class PokemonCardUiModel(
    val id: Int,
    val displayName: String,
    val imageUrl: String,
)

@Composable
fun PokemonCard(
    pokemon: PokemonCardUiModel,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewDetailDescription = stringResource(Res.string.card_view_detail, pokemon.displayName)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(pokemon.id) }
            .semantics { contentDescription = viewDetailDescription },
        shape = RoundedCornerShape(Radius.md),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = null, // the Card already describes the full set (accessibility)
                modifier = Modifier
                    .size(Sizing.cardImageSize)
                    .padding(Spacing.xs),
            )
            Text(
                text = pokemon.displayName.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
