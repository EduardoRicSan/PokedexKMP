package com.pokedex.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Espejo (parcial, solo los campos que consumimos) de GET /pokemon/{id}. */
@Serializable
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int, // decímetros
    val weight: Int, // hectogramos
    @SerialName("base_experience") val baseExperience: Int? = null,
    val types: List<PokemonTypeSlotDto>,
    val abilities: List<PokemonAbilitySlotDto>,
    val stats: List<PokemonStatSlotDto>,
    val sprites: PokemonSpritesDto,
)

@Serializable
data class PokemonTypeSlotDto(
    val slot: Int,
    val type: NamedResourceDto,
)

@Serializable
data class PokemonAbilitySlotDto(
    val ability: NamedResourceDto,
    @SerialName("is_hidden") val isHidden: Boolean = false,
    val slot: Int,
)

@Serializable
data class PokemonStatSlotDto(
    @SerialName("base_stat") val baseStat: Int,
    val effort: Int = 0,
    val stat: NamedResourceDto,
)

@Serializable
data class NamedResourceDto(
    val name: String,
    val url: String,
)

@Serializable
data class PokemonSpritesDto(
    @SerialName("front_default") val frontDefault: String? = null,
    val other: PokemonOtherSpritesDto? = null,
)

@Serializable
data class PokemonOtherSpritesDto(
    @SerialName("official-artwork") val officialArtwork: OfficialArtworkDto? = null,
)

@Serializable
data class OfficialArtworkDto(
    @SerialName("front_default") val frontDefault: String? = null,
)
