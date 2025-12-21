package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.requests.UpdateSystemCounterRequest

// Update system counter to specific value
class UpdateSystemCounterUseCase(private val repository: CounterRepository) {
    suspend operator fun invoke(request: UpdateSystemCounterRequest) {
        repository.updateSystemCounter(request.counterKey, request.count)
    }
}