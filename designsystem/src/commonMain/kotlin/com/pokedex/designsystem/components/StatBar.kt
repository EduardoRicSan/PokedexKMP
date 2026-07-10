package com.pokedex.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.pokedex.designsystem.tokens.Sizing
import com.pokedex.designsystem.tokens.Spacing

/**
 * Stat bar used in the detail (HP, Attack, Defense, etc.).
 * `maxReferenceValue` normalizes the bar visually (255 is the
 * theoretical maximum of a base stat in the Pokemon franchise).
 */
@Composable
fun StatBar(
    label: String,
    value: Int,
    maxReferenceValue: Int = 255,
    modifier: Modifier = Modifier,
) {
    val progress = (value.toFloat() / maxReferenceValue).coerceIn(0f, 1f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "$label: $value of $maxReferenceValue" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Text(
            text = label.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.width(96.dp),
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(Sizing.statBarHeight),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.width(32.dp),
        )
    }
}
