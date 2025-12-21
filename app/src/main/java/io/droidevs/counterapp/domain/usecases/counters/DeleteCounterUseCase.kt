package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.requests.DeleteCounterRequest
import kotlinx.coroutines.flow.firstOrNull

// Delete counter
class DeleteCounterUseCase(private val repository: CounterRepository) {
    suspend operator fun invoke(request: DeleteCounterRequest) {
        val counter = repository.getCounter(request.counterId)
            .firstOrNull()
        repository.deleteCounter(counter?: return)
    }
}