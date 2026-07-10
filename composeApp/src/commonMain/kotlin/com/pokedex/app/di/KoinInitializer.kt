package com.pokedex.app.di

import com.pokedex.core.di.coreModule
import com.pokedex.data.di.dataModule
import com.pokedex.domain.di.domainModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Punto único de arranque de Koin. Cada plataforma le pasa su propio
 * `platformModule` (que provee `DatabaseDriverFactory`, dependiente de
 * `Context` en Android) y llama a esta función una sola vez al iniciar.
 */
fun initKoin(platformModule: org.koin.core.module.Module, appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            coreModule,
            domainModule,
            dataModule,
            platformModule,
            appModule,
        )
    }
}
