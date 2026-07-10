package com.pokedex.domain.usecase

import com.pokedex.core.result.DataResult
import com.pokedex.domain.model.PokemonDetail
import com.pokedex.domain.repository.PokemonRepository

/** Use case: obtain the full detail of a Pokemon by id. */
class GetPokemonDetailUseCase(
    private val repository: PokemonRepository,
) {
    suspend operator fun invoke(id: Int): DataResult<PokemonDetail> =
        repository.getPokemonDetail(id)
}
