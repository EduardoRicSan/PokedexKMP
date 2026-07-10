package com.pokedex.data.repository

import com.pokedex.core.error.AppError
import com.pokedex.core.result.DataResult
import com.pokedex.data.local.PokemonLocalDataSource
import com.pokedex.data.local.toDomainDetail
import com.pokedex.data.mapper.toAppError
import com.pokedex.data.mapper.toDomain
import com.pokedex.data.remote.PokemonRemoteDataSource
import com.pokedex.domain.model.Pokemon
import com.pokedex.domain.model.PokemonDetail
import com.pokedex.domain.repository.PokemonRepository

/**
 * Single implementation of [PokemonRepository]: coordinates network and local cache
 * WITHOUT any upper layer knowing that both sources exist.
 *
 * Consistency strategy / partial offline:
 *  - "Network first, cache as backup": we always try to bring fresh data
 *    from the API; if the call fails (no connection, timeout, 5xx),
 *    we return the last cached instead of an error, to maintain a
 *    partial offline experience.
 *  - Only if the cache ALSO fails (e.g. first execution without previous
 *    data) the real [AppError] is propagated to the UI.
 *  - Each successful list page is persisted to be able to recover it
 *    offline later; the detail is cached the same way, per Pokemon.
 */
class PokemonRepositoryImpl(
    private val remoteDataSource: PokemonRemoteDataSource,
    private val localDataSource: PokemonLocalDataSource,
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): DataResult<List<Pokemon>> {
        return try {
            val remoteList = remoteDataSource.fetchPokemonList(limit, offset).results.map { it.toDomain() }
            localDataSource.cacheList(remoteList, offset)
            DataResult.Success(remoteList)
        } catch (throwable: Throwable) {
            val cached = safeGetCachedList(offset)
            if (cached.isNotEmpty()) {
                DataResult.Success(cached)
            } else {
                DataResult.Error(throwable.toAppError())
            }
        }
    }

    override suspend fun getPokemonDetail(id: Int): DataResult<PokemonDetail> {
        return try {
            val detail = remoteDataSource.fetchPokemonDetail(id).toDomain()
            localDataSource.cacheDetail(detail)
            DataResult.Success(detail)
        } catch (throwable: Throwable) {
            val cached = safeGetCachedDetail(id)
            if (cached != null) {
                DataResult.Success(cached)
            } else {
                DataResult.Error(throwable.toAppError())
            }
        }
    }

    private suspend fun safeGetCachedList(offset: Int): List<Pokemon> = try {
        localDataSource.getCachedList()
            .filter { it.pageOffset == offset.toLong() }
            .map { it.toDomain() }
    } catch (_: Throwable) {
        emptyList()
    }

    private suspend fun safeGetCachedDetail(id: Int): PokemonDetail? = try {
        localDataSource.getCachedDetail(id)?.toDomainDetail()
    } catch (_: Throwable) {
        null
    }
}
