package com.pokedex.data.mapper

import com.pokedex.data.local.db.PokemonEntity
import com.pokedex.data.remote.dto.PokemonDetailDto
import com.pokedex.data.remote.dto.PokemonListItemDto
import com.pokedex.domain.model.Pokemon
import com.pokedex.domain.model.PokemonDetail
import com.pokedex.domain.model.PokemonStat

/**
 * ESTRATEGIA DE IMAGEN (ver justificación completa en README.md):
 * GET /pokemon?limit&offset NO trae imagen, solo `name` + `url`.
 * `url` siempre tiene forma "https://pokeapi.co/api/v2/pokemon/{id}/".
 * En vez de pedir el detalle de cada uno de los 20 resultados (N+1
 * requests), extraemos el id del propio `url` y construimos la URL del
 * sprite estático del repositorio oficial de sprites de PokéAPI. Es una
 * función pura, determinística y sin llamadas de red adicionales.
 */
internal fun extractIdFromUrl(url: String): Int =
    url.trimEnd('/').substringAfterLast('/').toIntOrNull()
        ?: error("No se pudo extraer el id del pokemon desde url=$url")

internal fun spriteUrl(id: Int): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

internal fun officialArtworkUrl(id: Int): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

fun PokemonListItemDto.toDomain(): Pokemon {
    val id = extractIdFromUrl(url)
    return Pokemon(
        id = id,
        name = name,
        imageUrl = spriteUrl(id),
    )
}

fun PokemonDetailDto.toDomain(): PokemonDetail {
    // Preferimos el artwork oficial si la API lo trae; si no, caemos al
    // sprite normal; si tampoco existe, derivamos por id como último fallback.
    val resolvedImageUrl = sprites.other?.officialArtwork?.frontDefault
        ?: sprites.frontDefault
        ?: officialArtworkUrl(id)

    return PokemonDetail(
        id = id,
        name = name,
        imageUrl = resolvedImageUrl,
        types = types.sortedBy { it.slot }.map { it.type.name },
        abilities = abilities.sortedBy { it.slot }.map { it.ability.name },
        stats = stats.map { PokemonStat(name = it.stat.name, baseValue = it.baseStat) },
        heightMeters = height / 10.0,   // la API da decímetros
        weightKg = weight / 10.0,       // la API da hectogramos
        baseExperience = baseExperience ?: 0,
    )
}

/** Mapea la entidad cacheada (SQLDelight) de vuelta a modelo de dominio. */
fun PokemonEntity.toDomain(): Pokemon = Pokemon(
    id = id.toInt(),
    name = name,
    imageUrl = imageUrl,
)
