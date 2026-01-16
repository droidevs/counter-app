package io.droidevs.counterapp.domain.result

import io.droidevs.counterapp.domain.result.errors.Error


typealias RootError = Error

sealed interface Result<out D, out E : RootError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E : RootError>(val error: E) : Result<Nothing, E>

    // Extension functions for seamless handling
    fun <R> fold(
        onSuccess: (D) -> R,
        onFailure: (E) -> R
    ): R = when (this) {
        is Success -> onSuccess(data)
        is Failure -> onFailure(error)
    }

    fun getOrNull(): D? = fold({ it }, { null })
    fun errorOrNull(): E? = fold({ null }, { it })
}