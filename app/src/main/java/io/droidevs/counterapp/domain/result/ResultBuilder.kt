package io.droidevs.counterapp.domain.result

import kotlinx.coroutines.flow.Flow


@DslMarker
annotation class ResultBuild

@ResultBuild
class ResultBuilder<D, E : RootError> {
    fun build(block: () -> Result<D, E>): Result<D, E> = block()

    // Enables if/else conditions
    fun <D> buildIf(
        value: D?,
        onError: () -> E
    ): Result<D, E> = value?.let { Result.Success(it) }
        ?: Result.Failure(onError())

    // Enables iteration (though rarely needed for Results)
    fun <D> buildIterable(
        block: () -> Iterable<Result<D, E>>
    ): Result<List<D>, E> {
        val results = block().toList()
        return if (results.any { it is Result.Failure }) {
            Result.Failure((results.first { it is Result.Failure } as Result.Failure).error)
        } else {
            Result.Success(results.map { (it as Result.Success).data })
        }
    }

    fun <D1> Result<D1, E>.combine(
        @ResultBuild block: ResultBuilder<D, E>.(D1) -> Result<D, E>
    ): Result<D, E> = flatMap {
        ResultBuilder<D, E>().block(it)
    }

    fun <D1> combine(
        first: () -> Result<D1, E>,
        @ResultBuild block: ResultBuilder<D, E>.(D1) -> Result<D, E>
    ): Result<D, E> = first.invoke().flatMap {
        ResultBuilder<D, E>().block(it)
    }


    suspend fun <D1> Result<D1, E>.combineSuspended(
        @ResultBuild block: suspend ResultBuilder<D, E>.(D1) -> Result<D, E>,
    ): Result<D, E> = flatMapSuspended {
        ResultBuilder<D, E>().block(it)
    }

     suspend fun <D1> combineSuspended(
        first: suspend () -> Result<D1, E>,
        @ResultBuild block: suspend ResultBuilder<D, E>.(D1) -> Result<D, E>,
    ): Result<D, E> = first.invoke().flatMapSuspended {
        ResultBuilder<D, E>().block(it)
    }

    fun <D1, D2, D, E : RootError> combine(
        first: () -> Result<D1, E>,
        second: (D1) -> Result<D2, E>,
        @ResultBuild
        block: ResultBuilder<D, E>.(D1, D2) -> Result<D, E>
    ): Result<*, E> =
        first().flatMap { d1 ->
            second(d1).flatMap { d2 ->
                ResultBuilder<D, E>().block(d1, d2)
            }
        }


    /** Combine two Results with suspending transform */
    suspend fun <D1, D2, R> combineSuspended(
        first: suspend () -> Result<D1, E>,
        second: suspend (D1) -> Result<D2, E>,
        @ResultBuild
        block: suspend ResultBuilder<D, E>.(D1, D2) -> Result<D, E>
    ): Result<D, E> =
        first.invoke().flatMapSuspended { d1 ->
            second.invoke(d1).flatMapSuspended { d2 ->
                ResultBuilder<D, E>().block(d1, d2)
            }
        }

    /** Combine multiple Results in Iterable using flatMap chaining */
    fun <S> combineIterable(results: Iterable<Result<S, E>>, transform: (List<S>) -> Result<D, E>): Result<D, E> {
        val list = results.toList()
        return if (list.any { it is Result.Failure }) {
            Result.Failure((list.first { it is Result.Failure } as Result.Failure).error)
        } else {
            transform(list.map { (it as Result.Success).data })
        }
    }


    fun <R> Flow<Result<R, E>>.combine(
        block: ResultBuilder<D, E>.(R) -> Result<D, E>
    ): Flow<Result<D, E>> =
        flatMap { d1 ->
            ResultBuilder<D,E>().block(d1)
        }

    fun <R> Flow<Result<R, E>>.combineSuspended(
        block: suspend ResultBuilder<D, E>.(R) -> Result<D, E>
    ): Flow<Result<D, E>> =
        flatMapSuspended { d1 ->
            ResultBuilder<D,E>().block(d1)
        }

    fun <R> Flow<Result<R, E>>.combineFlow(
        block: ResultBuilder<D, E>.(R) -> Flow<Result<D, E>>
    ): Flow<Result<D, E>> =
        flatMapResult { d1 ->
            ResultBuilder<D,E>().block(d1)
        }

    fun <R> combineFlow(
        flow: () -> Flow<Result<R, E>>,
        block: ResultBuilder<D, E>.(R) -> Result<D, E>
    ): Flow<Result<D, E>> =
        flow.invoke().combine(block)


    suspend fun <R> combineFlowSuspended(
        flow: () -> Flow<Result<R, E>>,
        block: suspend ResultBuilder<D, E>.(R) -> Result<D, E>
    ): Flow<Result<D, E>> =
        flow.invoke().combineSuspended(block)


    fun <D1, D2, R> combineFlows(
        first: () -> Flow<Result<D1, E>>,
        second: (D1) -> Flow<Result<D2, E>>,
        block: ResultBuilder<D, E>.(D1, D2) -> Result<D, E>
    ): Flow<Result<D, E>> =
        first.invoke().flatMapResult { d1 ->
            second.invoke(d1).flatMap { d2 ->
                ResultBuilder<D,E>().block(d1, d2)
            }
        }

    /** Combine two Result flows with suspend transform */
    fun <D1, D2, R> combineFlowsSuspended(
        first: () -> Flow<Result<D1, E>>,
        second: (D1) -> Flow<Result<D2, E>>,
        block: suspend ResultBuilder<D, E>.(D1, D2) -> Result<D, E>
    ): Flow<Result<D, E>> =
        first.invoke().flatMapResult { d1 ->
            second.invoke(d1).flatMapSuspended { d2 ->
                ResultBuilder<D,E>().block(d1, d2)
            }
        }

}

