package com.pokedex.data.remote

import com.pokedex.data.remote.dto.PokemonDetailDto
import com.pokedex.data.remote.dto.PokemonListResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.call.body

class KtorPokemonRemoteDataSource(
    private val httpClient: HttpClient,
) : PokemonRemoteDataSource {

    override suspend fun fetchPokemonList(limit: Int, offset: Int): PokemonListResponseDto =
        httpClient.get("$BASE_URL/pokemon") {
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()

    override suspend fun fetchPokemonDetail(id: Int): PokemonDetailDto =
        httpClient.get("$BASE_URL/pokemon/$id").body()

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2"
    }
}
