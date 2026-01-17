package io.droidevs.counterapp.domain.result

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
} catch (e: Exception) {
    Result.Failure(errorTransform(e))
}

suspend fun <D , E : RootError> runCatchingWithResult(
    errorTransform: (Throwable) -> E,
    block: suspend () -> Result<D, E>
): Result<D, E> = try {
    block()
} catch (e: Exception) {
    Result.Failure(errorTransform(e))
}

// Base function using collect
fun <D, E : RootError> Flow<D>.asResult(
    errorTransform: (Throwable) -> E,
    retries: Int = 2,
    delayMillis: Long = 1000
): Flow<Result<D, E>> = flow {
    try {
        this@asResult
            .retry(retries.toLong()) { cause ->
                delay(delayMillis)
                cause is IOException // Only retry on network errors
            }
            .collect { item ->
                emit(Result.Success(item))
            }
    } catch (e: Exception) {
        emit(Result.Failure(errorTransform(e)))
    }
}

fun <D, E : RootError> Flow<Result<D, E>>.asResultAlready(
    errorTransform: (Throwable) -> E,
    retries: Int = 2,
    delayMillis: Long = 1000
): Flow<Result<D, E>> = flow {
    try {
        this@asResultAlready
            .retry(retries.toLong()) { cause ->
                delay(delayMillis)
                cause is IOException // Only retry on network errors
            }
            .collect { item ->
                emit(item)
            }
    } catch (e: Exception) {
        emit(Result.Failure(errorTransform(e)))
    }
}

// Now flowRunCatching can reuse asResultFlow
fun <D, E : RootError> flowRunCatching(
    errorTransform: (Throwable) -> E,
    block: suspend () -> Flow<D>
): Flow<Result<D, E>> = flow {
    try {
        block().asResult(errorTransform).collect { result ->
            emit(result)
        }
    } catch (e: Exception){
        emit(Result.Failure(errorTransform(e)))
    }
}