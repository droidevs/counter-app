package io.droidevs.counterapp.domain.services

sealed class ImportResult<out T> {
    data object Cancelled : ImportResult<Nothing>()

    data class Success<T>(val data: T) : ImportResult<T>()
    data class Error(val message: String) : ImportResult<Nothing>()
}
