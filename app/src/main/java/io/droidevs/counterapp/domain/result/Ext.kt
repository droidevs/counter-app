package io.droidevs.counterapp.domain.result

import androidx.annotation.RestrictTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

// Transformations
fun <D, E : RootError, R> Result<D, E>.map(
    transform: (D) -> R
): Result<R, E> =
    fold(
        onSuccess = { Result.Success(transform(it)) },
        onFailure = { Result.Failure(it) }
    )


fun <D, E : RootError, R> Result<D, E>.flatMap(
    transform: (D) -> Result<R, E>
): Result<R, E> =
    fold(
        onSuccess = transform,
        onFailure = { Result.Failure(it) }
    )

suspend fun <D, E : RootError, R> Result<D, E>.flatMapSuspended(
    action: suspend (D) -> Result<R, E>
): Result<R, E> =
    foldSuspend(
        onSuccess = action,
        onFailure = { Result.Failure(it) }
    )

fun <D, E : RootError, R> Flow<Result<D, E>>.flatMap(
    action: (D) -> Result<R, E>
): Flow<Result<R, E>> =
    transform { result ->
        emit(
            result.flatMap(
                transform  = action
            )
        )
    }

fun <D, E : RootError, R> Flow<Result<D, E>>.flatMapSuspended(
    action: suspend (D) -> Result<R, E>
): Flow<Result<R, E>> =
    transform { result ->
        emit(
            result.flatMapSuspended(
                action = action
            )
        )
    }

// Error recovery
fun <D, E : RootError> Result<D, E>.recover(
    transform: (E) -> D
): Result<D, Nothing> =
    fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Success(transform(it)) }
    )

fun <D, E : RootError> Result<D, E>.recoverWith(
    action: (E) -> Result<D, E>
): Result<D, E> =
    fold(
        onSuccess = { this },
        onFailure = action
    )

suspend fun <D, E : RootError> Result<D, E>.recoverWithSuspended(
    action: suspend (E) -> Result<D, E>
): Result<D, E> =
    foldSuspend(
        onSuccess = { this },
        onFailure = action
    )

fun <D, E : RootError> Flow<Result<D, E>>.recoverWith(
    action: (E) -> Result<D, E>
): Flow<Result<D, E>> =
    transform { result ->
        emit(
            result.recoverWith(
                action = action
            )
        )
    }

fun <D, E : RootError> Flow<Result<D, E>>.recoverWithSuspended(
    action: suspend (E) -> Result<D, E>
): Flow<Result<D, E>> =
    transform { result ->
        emit(
            result.recoverWithSuspended(
                action = action
            )
        )
    }

// Side-effects
fun <D, E : RootError> Result<D, E>.onSuccess(
    action: (D) -> Unit
): Result<D, E> =
    fold(
        onSuccess = {
            action(it)
            this
        },
        onFailure = { this }
    )

fun <D, E : RootError> Result<D, E>.onFailure(
    action: (E) -> Unit
): Result<D, E> =
    fold(
        onSuccess = { this },
        onFailure = {
            action(it)
            this
        }
    )

suspend fun <D, E : RootError> Result<D, E>.onSuccessSuspend(
    action: suspend (D) -> Unit
): Result<D, E> =
    foldSuspend(
        onSuccess = {
            action(it)
            this
        },
        onFailure = { this }
    )

suspend fun <D, E : RootError> Result<D, E>.onFailureSuspend(
    action: suspend (E) -> Unit
): Result<D, E> =
    foldSuspend(
        onSuccess = { this },
        onFailure = {
            action(it)
            this
        }
    ) 

fun <D, E : RootError> Flow<Result<D, E>>.onSuccess(
    action: (D) -> Unit
): Flow<Result<D, E>> = transform { result ->
    emit(
        result.fold(
            onSuccess = { data ->
                action(data)
                Result.Success(data)
            },
            onFailure = { Result.Failure(it) }
        )
    )
}


fun <D, E : RootError> Flow<Result<D, E>>.onFailure(
    action: (E) -> Unit
): Flow<Result<D, E>> = transform { result ->
    emit(
        result.fold(
            onSuccess = { Result.Success(it) },
            onFailure = { error ->
                action(error)
                Result.Failure(error)
            }
        )
    )
}

fun <D, E : RootError> Flow<Result<D, E>>.onSuccessSuspend(
    action: suspend (D) -> Unit
): Flow<Result<D, E>> = transform { result ->
    emit(
        result.foldSuspend(
            onSuccess = { data ->
                action(data)
                Result.Success(data)
            },
            onFailure = { Result.Failure(it) }
        )
    )
}


fun <D, E : RootError> Flow<Result<D, E>>.onFailureSuspend(
    action: suspend (E) -> Unit
): Flow<Result<D, E>> = transform { result ->
    emit(
        result.foldSuspend(
            onSuccess = { Result.Success(it) },
            onFailure = { error ->
                action(error)
                Result.Failure(error)
            }
        )
    )
}

fun <T, R, E : RootError> Flow<Result<T, E>>.mapResult(
    transform: (T) -> R
): Flow<Result<R, E>> =
    map { it.map(transform) }


fun <T, R, E : RootError> Flow<Result<T, E>>.flatMapResult(
    transform: (T) -> Flow<Result<R, E>>
): Flow<Result<R, E>> =
    flatMapConcat { result ->
        result.fold(
            onSuccess = transform,
            onFailure = { flowOf(Result.Failure(it)) }
        )
    }

//**



fun <T, E : RootError> Flow<Result<T, E>>.catchResult(
    transform: (Throwable) -> E
): Flow<Result<T, E>> =
    catch { emit(Result.Failure(transform(it))) }


fun <D> D.asSuccess(): Result.Success<D> = Result.Success(this)

fun <D, E : RootError> Result<D, E>.getOrNull(): D? =
    fold({ it }, { null })
