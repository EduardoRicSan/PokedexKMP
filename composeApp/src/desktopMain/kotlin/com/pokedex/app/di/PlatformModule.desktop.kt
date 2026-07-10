package com.pokedex.app.di

import com.pokedex.data.local.DatabaseDriverFactory
import org.koin.dsl.module

val platformModule = module {
    single { DatabaseDriverFactory() }
}
