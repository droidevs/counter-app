package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow


class GetLimitCountersUseCase(private val repository: CounterRepository) {
    operator fun invoke(limit: Int): Flow<List<Counter>> = repository.getLastEdited(limit)
}