package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSystemCountersPagedUseCase @Inject constructor(
    private val repository: CounterRepository,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(pageNumber: Int, pageSize: Int): Flow<Result<List<Counter>, DatabaseError>> =
        repository.getSystemCountersPaged(pageNumber, pageSize).flowOn(dispatchers.io)
}

