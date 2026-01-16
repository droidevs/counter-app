package io.droidevs.counterapp.domain.result


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
    fun <D2> combine(
        first: Result<D, E>,
        transform: (D) -> Result<D2, E>
    ): Result<D2, E> = first.flatMap(transform)
}

