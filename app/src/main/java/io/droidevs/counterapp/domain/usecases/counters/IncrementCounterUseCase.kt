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

class IncrementCounterUseCase @Inject constructor(
    private val resolveBehavior: ResolveCounterBehaviorUseCase,
    private val updateCounterUseCase: UpdateCounterUseCase,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(counter: Counter): Result<Unit, CounterDomainError> = withContext(dispatchers.io) {
        resultSuspend {
            combineSuspended(
                first = { resolveBehavior(counter).mapError { CounterDomainError.FailedToIncrement() } },
            ) { behavior ->
                val oldValue = counter.currentCount
                val newValue = oldValue + behavior.incrementStep

                val max = behavior.maxValue
                if (max != null && newValue > max) {
                    return@combineSuspended Result.Failure(CounterDomainError.IncrementBlockedByMaximum)
                }

                updateCounterUseCase(
                    UpdateCounterRequest(
                        counterId = counter.id,
                        newCount = newValue,
                        lastUpdatedAt = Instant.now()
                    )
                ).mapError { CounterDomainError.FailedToIncrement() }
            }
        }
    }
}
