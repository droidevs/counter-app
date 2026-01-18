package io.droidevs.counterapp.domain.result

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import java.io.IOException

fun <D, E : RootError> result(
    @ResultBuild block: ResultBuilder<D, E>.() -> Result<D, E>
): Result<D, E> {
    return ResultBuilder<D, E>().block()
}

suspend fun <D, E : RootError> resultSuspend(
    @ResultBuild block: suspend ResultBuilder<D, E>.() -> Result<D, E>
): Result<D, E> {
    return ResultBuilder<D, E>().block()
}

suspend fun <D, E : RootError> resultSuspendFromFlow(
    @ResultBuild block: suspend ResultBuilder<D, E>.() -> Flow<Result<D, E>>
): Result<D, E> {
    return ResultBuilder<D, E>().block().first()
}


fun <D, E : RootError> resultFlow(
    @ResultBuild block: ResultBuilder<D, E>.() -> Flow<Result<D, E>>
): Flow<Result<D, E>> {
    return ResultBuilder<D, E>().block()
}



suspend fun <D , E : RootError> runCatchingResult(
    errorTransform: (Throwable) -> E,
    block: suspend () -> D
): Result<D, E> = try {
    Result.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    Result.Failure(errorTransform(e))
}

suspend fun <D , E : RootError> runCatchingWithResult(
    errorTransform: (Throwable) -> E,
    block: suspend () -> Result<D, E>
): Result<D, E> = try {
    block()
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    Result.Failure(errorTransform(e))
}

// Base function using collect
fun <D, E : RootError> Flow<D>.asResult(
    errorTransform: (Throwable) -> E,
    retries: Int = 2,
    delayMillis: Long = 1000
): Flow<Result<D, E>> =
    this
        .retry(retries.toLong()) { cause ->
            delay(delayMillis)
            cause is IOException
        }
        .let { upstream ->
            flow<Result<D, E>> {
                upstream.collect { item ->
                    emit(Result.Success(item))
                }
            }
        }
        .catch { e ->
            if (e is CancellationException) throw e
            emit(Result.Failure(errorTransform(e)))
        }

fun <D, E : RootError> Flow<Result<D, E>>.asResultAlready(
    errorTransform: (Throwable) -> E,
    retries: Int = 2,
    delayMillis: Long = 1000
): Flow<Result<D, E>> =
    this
        .retry(retries.toLong()) { cause ->
            delay(delayMillis)
            cause is IOException
        }
        .catch { e ->
            if (e is CancellationException) throw e
            emit(Result.Failure(errorTransform(e)))
        }

fun <D, E : RootError> flowRunCatching(
    errorTransform: (Throwable) -> E,
    block: suspend () -> Flow<D>
): Flow<Result<D, E>> =
    flow {
        emitAll(block().asResult(errorTransform))
    }.catch { e ->
        if (e is CancellationException) throw e
        emit(Result.Failure(errorTransform(e)))
    }
