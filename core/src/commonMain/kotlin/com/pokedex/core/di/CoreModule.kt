package com.pokedex.core.di

import com.pokedex.core.dispatcher.AppDispatcherProvider
import com.pokedex.core.dispatcher.DispatcherProvider
import org.koin.dsl.module

val coreModule = module {
    single<DispatcherProvider> { AppDispatcherProvider() }
}
