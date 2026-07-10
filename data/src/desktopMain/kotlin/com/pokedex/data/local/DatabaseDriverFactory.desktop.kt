package com.pokedex.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.pokedex.data.local.db.PokedexDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:pokedex.db")
        try {
            PokedexDatabase.Schema.create(driver)
        } catch (e: Exception) {
            // Already created
        }
        return driver
    }
}
