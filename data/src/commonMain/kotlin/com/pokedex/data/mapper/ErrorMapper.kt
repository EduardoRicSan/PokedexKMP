package com.pokedex.data.mapper

import com.pokedex.core.error.AppError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

/**
 * Único punto donde una excepción de infraestructura se traduce a un
 * [AppError] del dominio. Mantenerlo centralizado evita que cada
 * datasource reinvente su propio mapeo, y facilita agregar un nuevo tipo
 * de excepción en un solo lugar.
 */
fun Throwable.toAppError(): AppError = when (this) {
    is HttpRequestTimeoutException -> AppError.Timeout

    is ClientRequestException -> {
        val code = response.status.value
        if (code == 404) {
            AppError.NotFound(resource = message ?: "resource")
        } else {
            AppError.ServerError(code = code, reason = message)
        }
    }

    is ServerResponseException -> AppError.ServerError(
        code = response.status.value,
        reason = message,
    )

    is SerializationException -> AppError.SerializationFailure(
        details = message ?: "formato de respuesta inesperado",
    )

    is IOException -> AppError.NoConnection

    else -> AppError.Unknown(message)
}
