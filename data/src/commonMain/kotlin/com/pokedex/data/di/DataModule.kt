package com.pokedex.data.di

import com.pokedex.data.local.PokemonLocalDataSource
import com.pokedex.data.local.SqlDelightPokemonLocalDataSource
import com.pokedex.data.remote.KtorPokemonRemoteDataSource
import com.pokedex.data.remote.PokemonRemoteDataSource
import com.pokedex.data.repository.PokemonRepositoryImpl
import com.pokedex.domain.repository.PokemonRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }
            install(Logging) { level = LogLevel.INFO }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
            }
        }
    }

    single<PokemonRemoteDataSource> { KtorPokemonRemoteDataSource(get()) }
    single<PokemonLocalDataSource> { SqlDelightPokemonLocalDataSource(get(), get()) }
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }
}
