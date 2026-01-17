package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.result.flatMap
import io.droidevs.counterapp.domain.result.flatMapSuspended
import io.droidevs.counterapp.domain.result.onSuccess
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCounterUseCase @Inject constructor(
    private val repository: CounterRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(request: UpdateCounterRequest): Result<Unit, DatabaseError> =
        withContext(dispatchers.io) {
            resultSuspendFromFlow {
                repository.getCounter(request.counterId)
                    .combineSuspended { existing ->
                        val updated = existing.apply {
                            name = request.newName ?: existing.name
                            categoryId = request.newCategoryId ?: existing.categoryId
                            currentCount = request.newCount ?: existing.currentCount
                            lastUpdatedAt = request.lastUpdatedAt ?: existing.lastUpdatedAt
                            orderAnchorAt = request.orderAnchorAt ?: existing.orderAnchorAt
                        }
                        repository.saveCounter(updated)
                    }
            }
        }
}
