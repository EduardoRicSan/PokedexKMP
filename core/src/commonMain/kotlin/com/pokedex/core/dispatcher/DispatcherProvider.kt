package com.pokedex.core.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Abstracción sobre los dispatchers de coroutines. Se inyecta vía Koin
 * en vez de usar Dispatchers.IO directamente, para poder sustituirlo por
 * un TestDispatcher en tests unitarios sin tocar el código de producción.
 */
interface DispatcherProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}

expect class AppDispatcherProvider() : DispatcherProvider
