package com.pokedex.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextAlign
import com.pokedex.designsystem.theme.PokedexColors
import com.pokedex.designsystem.tokens.Radius
import com.pokedex.designsystem.tokens.Spacing

@Composable
fun TypeChip(typeName: String, modifier: Modifier = Modifier) {
    val backgroundColor = PokedexColors.colorForType(typeName)
    val textColor = if (backgroundColor.luminance() > 0.5f) Color.Black else Color.White

    Text(
        text = typeName.replaceFirstChar { it.uppercase() },
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(Radius.pill))
            .padding(horizontal = Spacing.md, vertical = Spacing.xs),
    )
}
