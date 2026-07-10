package com.pokedex.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Espejo exacto de la respuesta de GET /pokemon?limit&offset.
 * OJO: la PokeAPI NO devuelve imagen aquí, solo name + url.
 * La resolución de imagen se hace en el mapper (ver PokemonMapper),
 * derivando el id desde `url`.
 */
@Serializable
data class PokemonListResponseDto(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonListItemDto>,
)

@Serializable
data class PokemonListItemDto(
    val name: String,
    val url: String,
)
