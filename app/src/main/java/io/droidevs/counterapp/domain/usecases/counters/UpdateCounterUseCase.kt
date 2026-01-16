package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCounterUseCase @Inject constructor(
    private val repository: CounterRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(request: UpdateCounterRequest) =
        withContext(dispatchers.io) {
            val existing = repository.getCounter(request.counterId)
                .firstOrNull()
            val updated = existing?.apply {
                name = request.newName ?: existing.name
                categoryId = request.newCategoryId ?: existing.categoryId
                currentCount = request.newCount ?: existing.currentCount
                lastUpdatedAt = request.lastUpdatedAt ?: existing.lastUpdatedAt
                orderAnchorAt = request.orderAnchorAt ?: existing.orderAnchorAt
            }
            updated?.let { repository.saveCounter(it) }
        }
}
