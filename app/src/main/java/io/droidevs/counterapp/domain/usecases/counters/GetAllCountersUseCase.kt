package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow

// Read operations
class GetAllCountersUseCase(private val repository: CounterRepository) {
    operator fun invoke(): Flow<List<Counter>> = repository.getAllCounters()
}