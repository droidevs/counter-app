package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllCountersUseCase @Inject constructor(
    private val repository: CounterRepository,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<List<Counter>> = repository.getAllCounters().flowOn(dispatchers.io)
}
