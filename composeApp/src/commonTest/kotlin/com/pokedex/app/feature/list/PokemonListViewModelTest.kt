package com.pokedex.app.feature.list

import com.pokedex.core.error.AppError
import com.pokedex.core.error.toUserMessage
import com.pokedex.core.result.DataResult
import com.pokedex.domain.model.Pokemon
import com.pokedex.domain.repository.PokemonRepository
import com.pokedex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class FakeRepository(
    private val pages: MutableMap<Int, DataResult<List<Pokemon>>>,
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int) =
        pages[offset] ?: DataResult.Success(emptyList())

    override suspend fun getPokemonDetail(id: Int) = throw NotImplementedError()
}

private fun pokemon(id: Int) = Pokemon(id, "pokemon-$id", "https://example.com/$id.png")

class PokemonListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() { Dispatchers.setMain(testDispatcher) }

    @AfterTest
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `carga inicial exitosa expone Success con los pokemon recibidos`() = runTest {
        val firstPage = (1..20).map { pokemon(it) }
        val repository = FakeRepository(mutableMapOf(0 to DataResult.Success(firstPage)))
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(repository))

        val state = viewModel.uiState.value

        assertTrue(state is PokemonListUiState.Success)
        assertEquals(20, (state as PokemonListUiState.Success).pokemons.size)
        assertEquals(false, state.endReached)
    }

    @Test
    fun `cuando la primera pagina falla, expone Error con mensaje mapeado`() = runTest {
        val repository = FakeRepository(mutableMapOf(0 to DataResult.Error(AppError.NoConnection)))
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(repository))

        val state = viewModel.uiState.value

        assertTrue(state is PokemonListUiState.Error)
        assertEquals(AppError.NoConnection.toUserMessage(), (state as PokemonListUiState.Error).message)
    }

    @Test
    fun `cuando la respuesta esta vacia, expone Empty`() = runTest {
        val repository = FakeRepository(mutableMapOf(0 to DataResult.Success(emptyList())))
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(repository))

        assertEquals(PokemonListUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `loadNextPage concatena la nueva pagina a la existente`() = runTest {
        val firstPage = (1..20).map { pokemon(it) }
        val secondPage = (21..30).map { pokemon(it) } // menor a pageSize => endReached
        val repository = FakeRepository(
            mutableMapOf(
                0 to DataResult.Success(firstPage),
                20 to DataResult.Success(secondPage),
            ),
        )
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(repository))

        viewModel.loadNextPage()

        val state = viewModel.uiState.value
        assertTrue(state is PokemonListUiState.Success)
        assertEquals(30, (state as PokemonListUiState.Success).pokemons.size)
        assertTrue(state.endReached)
    }
}
