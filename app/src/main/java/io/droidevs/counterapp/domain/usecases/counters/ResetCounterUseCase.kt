package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.errors.CounterDomainError
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.mapError
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.result.recoverWithSuspended
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

class ResetCounterUseCase @Inject constructor(
    private val resolveBehavior: ResolveCounterBehaviorUseCase,
    private val updateCounterUseCase: UpdateCounterUseCase,
    private val dispatchers: DispatcherProvider,
) {
    suspend operator fun invoke(counter: Counter): Result<Unit, CounterDomainError> = withContext(dispatchers.io) {
        resultSuspend {
            // Prefer per-counter default if present.
            val directDefault = counter.defaultValue
            if (directDefault != null) {
                return@resultSuspend updateCounterUseCase(
                    UpdateCounterRequest(
                        counterId = counter.id,
                        newCount = directDefault,
                        lastUpdatedAt = Instant.now(),
                    )
                ).mapError { CounterDomainError.FailedToReset() }
            }

            // Otherwise resolve behavior (may read global defaults if allowed).
            combineSuspended(
                first = { resolveBehavior(counter).mapError { CounterDomainError.FailedToReset() } },
            ) { behavior ->
                updateCounterUseCase(
                    UpdateCounterRequest(
                        counterId = counter.id,
                        newCount = behavior.defaultValue,
                        lastUpdatedAt = Instant.now(),
                    )
                ).mapError { CounterDomainError.FailedToReset() }
            }
                .recoverWithSuspended {
                    // If preference resolution fails, still allow a reset to 0.
                    updateCounterUseCase(
                        UpdateCounterRequest(
                            counterId = counter.id,
                            newCount = 0,
                            lastUpdatedAt = Instant.now(),
                        )
                    ).mapError { CounterDomainError.FailedToReset() }
                }
        }
    }
}
