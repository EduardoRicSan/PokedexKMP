package com.pokedex.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val abilities: List<String>,
    val stats: List<PokemonStat>,
    val heightMeters: Double,
    val weightKg: Double,
    val baseExperience: Int,
)

data class PokemonStat(
    val name: String,
    val baseValue: Int,
) {
    companion object {
        /** Reference value used to normalize the visual bar (0..1). */
        const val MAX_REFERENCE_VALUE = 255
    }
}
