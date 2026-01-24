package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.errors.CounterDomainError
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.mapError
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

class DecrementCounterUseCase @Inject constructor(
    private val resolveBehavior: ResolveCounterBehaviorUseCase,
    private val updateCounterUseCase: UpdateCounterUseCase,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(counter: Counter): Result<Unit, CounterDomainError> = withContext(dispatchers.io) {
        resultSuspend {
            combineSuspended(
                first = { resolveBehavior(counter).mapError { CounterDomainError.FailedToDecrement() } },
            ) { behavior ->
                val oldValue = counter.currentCount
                val newValue = oldValue - behavior.decrementStep

                val min = behavior.minValue
                if (min != null && newValue < min) {
                    return@combineSuspended Result.Failure(CounterDomainError.DecrementBlockedByMinimum)
                }

                updateCounterUseCase(
                    UpdateCounterRequest(
                        counterId = counter.id,
                        newCount = newValue,
                        lastUpdatedAt = Instant.now()
                    )
                ).mapError { CounterDomainError.FailedToDecrement() }
            }
        }
    }
}
