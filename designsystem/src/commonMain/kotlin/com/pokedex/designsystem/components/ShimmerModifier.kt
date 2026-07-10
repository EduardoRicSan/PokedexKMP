package com.pokedex.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pokedex.designsystem.theme.PokedexColors

/**
 * Modifier de shimmer reutilizado por TODOS los skeletons del design
 * system (PokemonCardSkeleton, DetailSkeleton, etc.) para que el efecto
 * visual de "cargando" sea consistente en toda la app sin duplicar la
 * animación en cada pantalla.
 */
fun Modifier.shimmerEffect(shape: Shape = RoundedCornerShape(8.dp)): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslate",
    )
    val brush = Brush.linearGradient(
        colors = listOf(
            PokedexColors.ShimmerBase,
            PokedexColors.ShimmerHighlight,
            PokedexColors.ShimmerBase,
        ),
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 400f, 400f),
    )
    this.background(brush = brush, shape = shape)
}

/** Bloque gris animado de tamaño fijo, building block de los skeletons. */
@Composable
fun ShimmerBox(width: Dp, height: Dp, shape: Shape = RoundedCornerShape(8.dp)) {
    Box(
        modifier = Modifier
            .size(width, height)
            .shimmerEffect(shape),
    )
}
