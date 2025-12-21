package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

// Update counter (partial update)
class UpdateCounterUseCase(private val repository: CounterRepository) {
    suspend operator fun invoke(request: UpdateCounterRequest) {
        val existing = repository.getCounter(request.counterId)
            .firstOrNull()
        val updated = existing?.apply {
            name = request.newName ?: existing.name
            categoryId = request.newCategoryId ?: existing.categoryId
            currentCount = request.newCount ?: existing.currentCount
        }
        repository.saveCounter(updated?: return)
    }
}