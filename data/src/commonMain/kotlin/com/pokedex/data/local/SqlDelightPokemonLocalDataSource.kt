package com.pokedex.data.local

import com.pokedex.core.dispatcher.DispatcherProvider
import com.pokedex.data.local.db.PokedexDatabase
import com.pokedex.data.local.db.PokemonDetailEntity
import com.pokedex.data.local.db.PokemonEntity
import com.pokedex.domain.model.Pokemon
import com.pokedex.domain.model.PokemonDetail
import com.pokedex.domain.model.PokemonStat
import kotlinx.coroutines.withContext

/**
 * Implementación de persistencia con SQLDelight.
 *
 * JUSTIFICACIÓN DE LA ESTRATEGIA (ver también README.md):
 * Se eligió SQLDelight sobre otras opciones (DataStore, archivos planos,
 * key-value) porque:
 *  1. Es multiplatforma real (Android/iOS/Desktop) con un solo esquema SQL.
 *  2. El dato es relacional y consultable (listado + detalle por id), lo
 *     cual encaja mejor en tablas que en un blob key-value.
 *  3. Genera APIs tipadas en tiempo de compilación, evitando errores de
 *     parsing manual de JSON en disco.
 *
 * Estrategia de consistencia: "cache-first en fallo, red-first cuando hay
 * conexión" (ver PokemonRepositoryImpl). Los tipos/habilidades del detalle
 * se guardan como CSV simple porque son listas pequeñas y de solo lectura;
 * no justifica tablas normalizadas adicionales para este alcance.
 */
class SqlDelightPokemonLocalDataSource(
    databaseDriverFactory: DatabaseDriverFactory,
    private val dispatcherProvider: DispatcherProvider,
) : PokemonLocalDataSource {

    private val database = PokedexDatabase(databaseDriverFactory.createDriver())
    private val queries = database.pokemonEntityQueries
    private val detailQueries = database.pokemonDetailEntityQueries

    override suspend fun getCachedList(): List<PokemonEntity> = withContext(dispatcherProvider.io) {
        queries.selectAllOrderedById().executeAsList()
    }

    override suspend fun cacheList(pokemons: List<Pokemon>, offset: Int) = withContext(dispatcherProvider.io) {
        database.transaction {
            pokemons.forEach { pokemon ->
                queries.insertOrReplace(
                    id = pokemon.id.toLong(),
                    name = pokemon.name,
                    imageUrl = pokemon.imageUrl,
                    pageOffset = offset.toLong(),
                )
            }
        }
    }

    override suspend fun getCachedDetail(id: Int): PokemonDetailEntity? = withContext(dispatcherProvider.io) {
        detailQueries.selectById(id.toLong()).executeAsOneOrNull()
    }

    override suspend fun cacheDetail(detail: PokemonDetail) = withContext(dispatcherProvider.io) {
        detailQueries.insertOrReplace(
            id = detail.id.toLong(),
            name = detail.name,
            imageUrl = detail.imageUrl,
            types = detail.types.joinToString(separator = ","),
            abilities = detail.abilities.joinToString(separator = ","),
            statsCsv = detail.stats.joinToString(separator = ";") { "${it.name}:${it.baseValue}" },
            heightMeters = detail.heightMeters,
            weightKg = detail.weightKg,
            baseExperience = detail.baseExperience.toLong(),
        )
    }
}

/** Reconstruye un [PokemonDetail] de dominio a partir de la fila cacheada. */
fun PokemonDetailEntity.toDomainDetail(): PokemonDetail = PokemonDetail(
    id = id.toInt(),
    name = name,
    imageUrl = imageUrl,
    types = types.split(",").filter { it.isNotBlank() },
    abilities = abilities.split(",").filter { it.isNotBlank() },
    stats = statsCsv.split(";").filter { it.isNotBlank() }.map { entry ->
        val (statName, value) = entry.split(":")
        PokemonStat(name = statName, baseValue = value.toInt())
    },
    heightMeters = heightMeters,
    weightKg = weightKg,
    baseExperience = baseExperience.toInt(),
)
