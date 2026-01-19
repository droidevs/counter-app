package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
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
                            canIncrease = request.canIncrease ?: existing.canIncrease
                            canDecrease = request.canDecrease ?: existing.canDecrease

                            // IMPORTANT:
                            // We must allow explicitly clearing overrides (setting them to null).
                            // Therefore: only update these fields if the request provides them (including null).
                            if (request.useDefaultBehavior != null) {
                                useDefaultBehavior = request.useDefaultBehavior
                            }
                            if (request.incrementStep != null || request.useDefaultBehavior != null) {
                                // If caller toggles useDefaultBehavior, they may also send explicit nulls.
                                // Respect whatever is passed in request.
                                incrementStep = request.incrementStep
                            }
                            if (request.decrementStep != null || request.useDefaultBehavior != null) {
                                decrementStep = request.decrementStep
                            }
                            if (request.minValue != null || request.useDefaultBehavior != null) {
                                minValue = request.minValue
                            }
                            if (request.maxValue != null || request.useDefaultBehavior != null) {
                                maxValue = request.maxValue
                            }
                            if (request.defaultValue != null || request.useDefaultBehavior != null) {
                                defaultValue = request.defaultValue
                            }

                            lastUpdatedAt = request.lastUpdatedAt ?: existing.lastUpdatedAt
                            orderAnchorAt = request.orderAnchorAt ?: existing.orderAnchorAt
                        }
                        repository.saveCounter(updated)
                    }
            }
        }
}
