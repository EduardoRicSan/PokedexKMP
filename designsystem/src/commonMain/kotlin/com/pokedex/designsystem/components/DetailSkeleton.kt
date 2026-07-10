package com.pokedex.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pokedex.designsystem.tokens.Radius
import com.pokedex.designsystem.tokens.Sizing
import com.pokedex.designsystem.tokens.Spacing

/**
 * Detail screen skeleton: large image, title, type chips and
 * simulated stat bars, respecting the same proportions as the
 * real screen to avoid layout jumping.
 */
@Composable
fun DetailSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        ShimmerBox(
            width = Sizing.detailImageSize,
            height = Sizing.detailImageSize,
            shape = RoundedCornerShape(Radius.lg),
        )
        ShimmerBox(width = 160.dp, height = 24.dp)
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            ShimmerBox(width = 64.dp, height = 28.dp, shape = RoundedCornerShape(Radius.pill))
            ShimmerBox(width = 64.dp, height = 28.dp, shape = RoundedCornerShape(Radius.pill))
        }
        repeat(6) {
            ShimmerBox(width = 280.dp, height = 16.dp)
        }
    }
}
