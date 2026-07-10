package com.pokedex.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.pokedex.data.local.db.PokedexDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(PokedexDatabase.Schema, "pokedex.db")
}
