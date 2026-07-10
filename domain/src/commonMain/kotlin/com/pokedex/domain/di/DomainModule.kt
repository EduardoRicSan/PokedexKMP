package com.pokedex.domain.di

import com.pokedex.domain.usecase.GetPokemonDetailUseCase
import com.pokedex.domain.usecase.GetPokemonListUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }
}
