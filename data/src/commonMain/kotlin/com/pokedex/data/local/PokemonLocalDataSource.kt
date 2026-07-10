package com.pokedex.data.local

import com.pokedex.data.local.db.PokemonDetailEntity
import com.pokedex.data.local.db.PokemonEntity
import com.pokedex.domain.model.Pokemon
import com.pokedex.domain.model.PokemonDetail

/**
 * Puerto del datasource local. El repositorio depende de esta interfaz,
 * no de SQLDelight directamente, permitiendo sustituir la estrategia de
 * persistencia (por ejemplo, a Room o a un simple archivo) sin tocar
 * el repositorio ni las capas superiores.
 */
interface PokemonLocalDataSource {
    suspend fun getCachedList(): List<PokemonEntity>
    suspend fun cacheList(pokemons: List<Pokemon>, offset: Int)
    suspend fun getCachedDetail(id: Int): PokemonDetailEntity?
    suspend fun cacheDetail(detail: PokemonDetail)
}
