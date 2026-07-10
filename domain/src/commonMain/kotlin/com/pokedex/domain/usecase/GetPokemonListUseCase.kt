package com.pokedex.domain.usecase

import com.pokedex.core.result.DataResult
import com.pokedex.domain.model.Pokemon
import com.pokedex.domain.repository.PokemonRepository

/**
 * Use case: obtain a page of the Pokemon list.
 *
 * It is deliberately kept thin (Single Responsibility): it only
 * orchestrates the call to the repository. If in the future additional
 * business logic is needed (e.g. filtering by type, sorting alphabetically),
 * this is the only place where it would be added, without touching
 * ViewModel or Repository.
 */
class GetPokemonListUseCase(
    private val repository: PokemonRepository,
) {
    suspend operator fun invoke(limit: Int = DEFAULT_PAGE_SIZE, offset: Int = 0): DataResult<List<Pokemon>> =
        repository.getPokemonList(limit = limit, offset = offset)

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
