package com.pokedex.core.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class AppDispatcherProvider actual constructor() : DispatcherProvider {
    // On iOS, Dispatchers.IO might be internal or not fully supported in some configurations.
    // Dispatchers.Default is a safe background dispatcher to use as a fallback.
    override val io: CoroutineDispatcher = Dispatchers.Default
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
}
