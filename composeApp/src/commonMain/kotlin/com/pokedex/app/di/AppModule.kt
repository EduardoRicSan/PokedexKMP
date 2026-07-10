package com.pokedex.app.di

import com.pokedex.app.feature.detail.PokemonDetailViewModel
import com.pokedex.app.feature.list.PokemonListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { PokemonListViewModel(get()) }

    viewModel { params ->
        PokemonDetailViewModel(
            pokemonId = params.get(),
            getPokemonDetailUseCase = get(),
        )
    }
}
