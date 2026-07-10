package com.pokedex.designsystem.tokens

import androidx.compose.ui.unit.dp

/**
 * Tokens de espaciado nombrados. Ningún componente del design system
 * (ni pantallas que lo consuman) debería escribir `.dp` con un número
 * mágico directamente para paddings/gaps: siempre a través de estos
 * valores, para que un cambio de escala global sea de un solo lugar.
 */
object Spacing {
    val xxs = 2.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
    val xxxl = 48.dp
}

object Radius {
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val pill = 999.dp
}

object Sizing {
    val cardImageSize = 120.dp
    val detailImageSize = 200.dp
    val statBarHeight = 8.dp
    val minTouchTarget = 48.dp
}
