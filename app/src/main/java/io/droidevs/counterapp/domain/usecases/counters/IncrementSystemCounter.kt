package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.requests.IncrementSystemCounterRequest

// Increment/decrement system counter
class IncrementSystemCounterUseCase(private val repository: CounterRepository) {
    suspend operator fun invoke(request: IncrementSystemCounterRequest) {
        repository.incrementSystemCounter(request.counterKey)
    }
}