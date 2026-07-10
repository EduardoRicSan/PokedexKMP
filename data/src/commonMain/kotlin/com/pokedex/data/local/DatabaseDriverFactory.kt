package com.pokedex.data.local

import app.cash.sqldelight.db.SqlDriver

/**
 * Cada plataforma sabe cómo crear su propio SqlDriver (AndroidSqliteDriver,
 * NativeSqliteDriver para iOS, JdbcSqliteDriver para Desktop). El resto de
 * `data` trabaja únicamente contra la interfaz común de SQLDelight.
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
