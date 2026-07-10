package com.pokedex.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pokedex.designsystem.tokens.Radius
import com.pokedex.designsystem.tokens.Sizing
import com.pokedex.designsystem.tokens.Spacing

/**
 * Skeleton del listado. A propósito comparte EXACTAMENTE el mismo padding,
 * tamaño de imagen (Sizing.cardImageSize) y estructura que [PokemonCard],
 * para que al llegar la data real no haya ningún "salto" visual de layout.
 */
@Composable
fun PokemonCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
            ShimmerBox(
                width = Sizing.cardImageSize,
                height = Sizing.cardImageSize,
                shape = RoundedCornerShape(Radius.sm),
            )
            ShimmerBox(width = 72.dp, height = 16.dp)
        }
    }
}
