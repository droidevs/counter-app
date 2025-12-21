package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.requests.CreateCounterRequest

// Create counter
class CreateCounterUseCase(private val repository: CounterRepository) {
    suspend operator fun invoke(request: CreateCounterRequest) {
        repository.createCounter(request.counter)
    }
}