package com.pokedex.domain.usecase

import com.pokedex.core.error.AppError
import com.pokedex.core.result.DataResult
import com.pokedex.domain.model.Pokemon
import com.pokedex.domain.repository.PokemonRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Fake de PokemonRepository. Al depender domain solo de la interfaz
 * (Dependency Inversion), testear el UseCase no requiere Ktor, SQLDelight
 * ni ningún framework: un fake en memoria es suficiente.
 */
private class FakePokemonRepository(
    private val result: DataResult<List<Pokemon>>,
) : PokemonRepository {
    var lastLimit: Int? = null
    var lastOffset: Int? = null

    override suspend fun getPokemonList(limit: Int, offset: Int): DataResult<List<Pokemon>> {
        lastLimit = limit
        lastOffset = offset
        return result
    }

    override suspend fun getPokemonDetail(id: Int) =
        throw NotImplementedError("No usado en este test")
}

class GetPokemonListUseCaseTest {

    @Test
    fun `cuando el repositorio responde con exito, devuelve la lista tal cual`() = kotlinx.coroutines.test.runTest {
        val expected = listOf(
            Pokemon(id = 1, name = "bulbasaur", imageUrl = "https://example.com/1.png"),
            Pokemon(id = 2, name = "ivysaur", imageUrl = "https://example.com/2.png"),
        )
        val repository = FakePokemonRepository(DataResult.Success(expected))
        val useCase = GetPokemonListUseCase(repository)

        val result = useCase(limit = 20, offset = 0)

        assertTrue(result is DataResult.Success)
        assertEquals(expected, (result as DataResult.Success).data)
    }

    @Test
    fun `pasa correctamente limit y offset al repositorio`() = kotlinx.coroutines.test.runTest {
        val repository = FakePokemonRepository(DataResult.Success(emptyList()))
        val useCase = GetPokemonListUseCase(repository)

        useCase(limit = 20, offset = 40)

        assertEquals(20, repository.lastLimit)
        assertEquals(40, repository.lastOffset)
    }

    @Test
    fun `usa el tamano de pagina por defecto cuando no se especifica`() = kotlinx.coroutines.test.runTest {
        val repository = FakePokemonRepository(DataResult.Success(emptyList()))
        val useCase = GetPokemonListUseCase(repository)

        useCase()

        assertEquals(GetPokemonListUseCase.DEFAULT_PAGE_SIZE, repository.lastLimit)
        assertEquals(0, repository.lastOffset)
    }

    @Test
    fun `cuando el repositorio falla, propaga el AppError sin transformarlo`() = kotlinx.coroutines.test.runTest {
        val repository = FakePokemonRepository(DataResult.Error(AppError.NoConnection))
        val useCase = GetPokemonListUseCase(repository)

        val result = useCase()

        assertTrue(result is DataResult.Error)
        assertEquals(AppError.NoConnection, (result as DataResult.Error).error)
    }
}
