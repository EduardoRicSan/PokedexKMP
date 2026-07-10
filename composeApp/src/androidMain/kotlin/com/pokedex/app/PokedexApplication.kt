package com.pokedex.app

import android.app.Application
import com.pokedex.app.di.initKoin
import com.pokedex.app.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class PokedexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(platformModule = platformModule) {
            androidLogger()
            androidContext(this@PokedexApplication)
        }
    }
}
