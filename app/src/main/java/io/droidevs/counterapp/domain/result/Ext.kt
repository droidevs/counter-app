package io.droidevs.counterapp.domain.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

// Transformations
fun <D, E : RootError, R> Result<D, E>.map(transform: (D) -> R): Result<R, E> =
    fold({ Result.Success(transform(it)) }, { Result.Failure(it) })



//fun <D, E : RootError, R> Flow<Result<D, E>>.map(transform: (D) -> R): Flow<Result<R, E>> = flow{
//    this@map.collect {
//        emit(it.map(transform))
//    }
//}

fun <D, E : RootError, R> Result<D, E>.flatMap(transform: (D) -> Result<R, E>): Result<R, E> =
    fold(transform, { Result.Failure(it) })

// Error recovery
fun <D, E : RootError> Result<D, E>.recover(transform: (E) -> D): Result<D, Nothing> =
    fold({ Result.Success(it) }, { Result.Success(transform(it)) })

// Side-effects
fun <D, E : RootError> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> =
    also { if (it is Result.Success) action(it.data) }

fun <D, E : RootError> Result<D, E>.onSuccessWithResult(action: (D) -> Result<D, E>): Result<D, E> =
    if (this is Result.Success) action(this.data) else this

fun <D, E : RootError> Flow<Result<D, E>>.onSuccessWithResult(
    action: suspend (D) -> Result<D, E>
): Flow<Result<D, E>> = transform { result ->
    if (result is Result.Success) {
        emit(action(result.data))
    }
    else
        emit(result)
}

fun <D, E : RootError> Flow<Result<D, E>>.onFailureWithResult(
    action: suspend (E) -> Result<D, E>
): Flow<Result<D, E>> = transform { result ->
    if (result is Result.Failure) {
        emit(action(result.error))
    }
    else
        emit(result)
}

fun <D, E : RootError> Flow<Result<D, E>>.onSuccess(
    transform: (Throwable) -> E,
    action: suspend (D) -> Unit
): Flow<Result<D, E>> = transform { result ->
    when (result) {
        is Result.Success -> {
            try {
                action(result.data)
                emit(result) // Emit original success if action succeeds
            } catch (e: Throwable) {
                emit(Result.Failure(transform(e)))
            }
        }
        is Result.Failure -> emit(result)
    }
}

fun <D, E : RootError> Flow<Result<D, E>>.onFailure(
    transform: (Throwable) -> E,
    action: suspend (E) -> Unit
): Flow<Result<D, E>> = transform { result ->
    when (result) {
        is Result.Failure -> {
            try {
                action(result.error)
                emit(result) // Emit original failure if action succeeds
            } catch (e: Throwable) {
                emit(Result.Failure(transform(e)))
            }
        }
        is Result.Success -> emit(result)
    }
}

fun <D, E : RootError> Result<D, E>.onFailure(action: (E) -> Unit): Result<D, E> =
    also { if (it is Result.Failure) action(it.error) }

fun <D, E : RootError> Result<D, E>.onFailureWithResult(action: (E) -> Result<D, E>): Result<D, E> =
    if (this is Result.Failure) action(this.error) else this

suspend fun <D, E : RootError> Result<D, E>.onSuccessSuspend(action: suspend (D) -> Unit): Result<D, E> =
    also { if (it is Result.Success) action(it.data) }

suspend fun <D, E : RootError> Result<D, E>.onSuccessSuspendWithResult(action: suspend (D) -> Result<D, E>): Result<D, E> =
    if (this is Result.Success) action(this.data) else this

suspend fun <D, E : RootError> Result<D, E>.onFailureSuspend(action: suspend (E) -> Unit): Result<D, E> =
    also { if (it is Result.Failure) action(it.error) }

suspend fun <D, E : RootError> Result<D, E>.onFailureSuspendWithResult(action: suspend (E) -> Result<D, E>): Result<D, E> =
    if (this is Result.Failure) action(this.error) else this

fun <T, E : RootError> Flow<Result<T, E>>.catchResult(
    transform: (Throwable) -> E
): Flow<Result<T, E>> = catch { e -> emit(Result.Failure(transform(e))) }

fun <T, R, E : RootError> Flow<Result<T, E>>.mapResult(
    transform: (T) -> R
): Flow<Result<R, E>> = map { result ->
    when (result) {
        is Result.Success -> Result.Success(transform(result.data))
        is Result.Failure -> Result.Failure(result.error)
    }
}

fun <T, E : RootError, R> Flow<Result<T, E>>.flatMapResult(
    transform: (T) -> Flow<Result<R, E>>
): Flow<Result<R, E>> = flatMapConcat { result ->
    when (result) {
        is Result.Success -> transform(result.data)
        is Result.Failure -> flowOf(Result.Failure(result.error))
    }
}