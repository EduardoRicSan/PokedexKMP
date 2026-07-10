package com.pokedex.data.remote

import com.pokedex.data.remote.dto.PokemonDetailDto
import com.pokedex.data.remote.dto.PokemonListResponseDto

/**
 * Puerto del datasource remoto. Se define como interfaz para que el
 * Repository dependa de una abstracción y no de Ktor directamente
 * (Dependency Inversion) y para poder mockearlo fácilmente en tests.
 */
interface PokemonRemoteDataSource {
    suspend fun fetchPokemonList(limit: Int, offset: Int): PokemonListResponseDto
    suspend fun fetchPokemonDetail(id: Int): PokemonDetailDto
}
