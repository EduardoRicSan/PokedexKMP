package com.pokedex.core.error

/**
 * Represents all possible application errors already translated
 * into a domain vocabulary. No layer above `data` should
 * see a raw Ktor, SQLDelight, etc. exception: it always arrives as
 * a mapped [AppError], ready to be converted into a UI message.
 *
 * Purposely sealed: the `when` in the presentation layer is exhaustive
 * and the compiler warns if a new error type is added without handling it.
 */
sealed class AppError {

    /** No internet connection or the request did not complete. */
    data object NoConnection : AppError()

    /** The server responded, but with an error code (4xx/5xx). */
    data class ServerError(val code: Int, val reason: String? = null) : AppError()

    /** The requested resource does not exist (typically 404). */
    data class NotFound(val resource: String) : AppError()

    /** The request timed out. */
    data object Timeout : AppError()

    /** Error parsing/serializing the response (unexpected API contract). */
    data class SerializationFailure(val details: String) : AppError()

    /** Local database read/write error. */
    data class LocalStorageError(val details: String) : AppError()

    /** Any unanticipated error; keeps the original message for logs/debug. */
    data class Unknown(val message: String?) : AppError()
}

/**
 * Translates an [AppError] into a readable message for the final user.
 * Centralized here so that both list and detail ViewModels
 * use exactly the same copy, avoiding inconsistent messages.
 * 
 * Note: Hardcoded strings are kept here as a fallback; UI-specific 
 * translations are handled in the ViewModels using Compose Resources.
 */
fun AppError.toUserMessage(): String = when (this) {
    is AppError.NoConnection -> "No internet connection. Please check your network and try again."
    is AppError.Timeout -> "The request took too long. Please try again."
    is AppError.NotFound -> "The requested information was not found."
    is AppError.ServerError -> "A server problem occurred (code $code). Please try again later."
    is AppError.SerializationFailure -> "Could not interpret the server response."
    is AppError.LocalStorageError -> "Could not access data saved on the device."
    is AppError.Unknown -> "An unexpected error occurred. Please try again."
}
