package com.pokedex.core.result

import com.pokedex.core.error.AppError

/**
 * Wrapper explícito de éxito/error, equivalente a un Either<AppError, T>.
 *
 * Se prefiere sobre el `Result<T>` de Kotlin porque el `Throwable` de
 * `Result` no obliga a mapear a un vocabulario propio de la app: con
 * `DataResult` el error SIEMPRE es un [AppError] ya traducido, lo que
 * evita que excepciones de infraestructura (Ktor, SQLDelight) se filtren
 * hasta la capa de presentación.
 */
sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val error: AppError) : DataResult<Nothing>()

    inline fun <R> map(transform: (T) -> R): DataResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
    }

    inline fun onSuccess(action: (T) -> Unit): DataResult<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (AppError) -> Unit): DataResult<T> {
        if (this is Error) action(error)
        return this
    }
}

inline fun <T> dataResultOf(block: () -> T, mapError: (Throwable) -> AppError): DataResult<T> =
    try {
        DataResult.Success(block())
    } catch (t: Throwable) {
        DataResult.Error(mapError(t))
    }
