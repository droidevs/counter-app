package io.droidevs.counterapp.domain.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Error-channel mapping helpers.
 *
 * These are preferred over using fold(...) directly when you want to convert one error type to another.
 */

fun <D, E : RootError, E2 : RootError> Result<D, E>.mapError(
    transform: (E) -> E2
): Result<D, E2> = when (this) {
    is Result.Success -> Result.Success(data)
    is Result.Failure -> Result.Failure(transform(error))
}

fun <D, E : RootError, E2 : RootError> Flow<Result<D, E>>.mapError(
    transform: (E) -> E2
): Flow<Result<D, E2>> = map { it.mapError(transform) }

