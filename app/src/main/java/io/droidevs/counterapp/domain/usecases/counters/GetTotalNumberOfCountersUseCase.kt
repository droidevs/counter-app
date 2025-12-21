package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow

class GetTotalNumberOfCountersUseCase(private val repository: CounterRepository) {
    operator fun invoke(): Flow<Int> = repository.getTotalCounters()
}