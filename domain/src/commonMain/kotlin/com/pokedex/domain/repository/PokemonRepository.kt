package com.pokedex.domain.repository

import com.pokedex.core.result.DataResult
import com.pokedex.domain.model.Pokemon
import com.pokedex.domain.model.PokemonDetail

/**
 * Port (in terms of Clean Architecture / Ports & Adapters) that the
 * domain exposes and that the `data` layer implements (Dependency Inversion:
 * the domain does not depend on Ktor/SQLDelight, it is the other way around).
 *
 * Each method can be resolved from the network or from local cache depending on the
 * implementation strategy; this interface is indifferent to that.
 */
interface PokemonRepository {

    /**
     * Obtains a page of the Pokemon list.
     * @param limit number of elements to bring.
     * @param offset starting point (for incremental pagination).
     */
    suspend fun getPokemonList(limit: Int, offset: Int): DataResult<List<Pokemon>>

    /** Obtains the full detail of a Pokemon by its id. */
    suspend fun getPokemonDetail(id: Int): DataResult<PokemonDetail>
}
