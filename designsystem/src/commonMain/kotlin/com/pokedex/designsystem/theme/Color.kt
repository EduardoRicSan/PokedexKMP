package com.pokedex.designsystem.theme

import androidx.compose.ui.graphics.Color

object PokedexColors {
    val PokedexRed = Color(0xFFE3350D)
    val PokedexRedDark = Color(0xFFB8290A)
    val PokedexBlue = Color(0xFF3B4CCA)
    val PokedexYellow = Color(0xFFFFCB05)

    val BackgroundLight = Color(0xFFF7F7FA)
    val SurfaceLight = Color(0xFFFFFFFF)
    val OnSurfaceLight = Color(0xFF1B1B1F)

    val BackgroundDark = Color(0xFF121214)
    val SurfaceDark = Color(0xFF1E1E22)
    val OnSurfaceDark = Color(0xFFE5E5EA)

    val ShimmerBase = Color(0xFFE0E0E0)
    val ShimmerHighlight = Color(0xFFF5F5F5)

    val ErrorRed = Color(0xFFBA1A1A)

    /** Colores oficiales por tipo de Pokémon, usados en TypeChip. */
    val typeColors: Map<String, Color> = mapOf(
        "normal" to Color(0xFFA8A878),
        "fire" to Color(0xFFF08030),
        "water" to Color(0xFF6890F0),
        "electric" to Color(0xFFF8D030),
        "grass" to Color(0xFF78C850),
        "ice" to Color(0xFF98D8D8),
        "fighting" to Color(0xFFC03028),
        "poison" to Color(0xFFA040A0),
        "ground" to Color(0xFFE0C068),
        "flying" to Color(0xFFA890F0),
        "psychic" to Color(0xFFF85888),
        "bug" to Color(0xFFA8B820),
        "rock" to Color(0xFFB8A038),
        "ghost" to Color(0xFF705898),
        "dragon" to Color(0xFF7038F8),
        "dark" to Color(0xFF705848),
        "steel" to Color(0xFFB8B8D0),
        "fairy" to Color(0xFFEE99AC),
    )

    fun colorForType(type: String): Color = typeColors[type.lowercase()] ?: PokedexBlue
}
