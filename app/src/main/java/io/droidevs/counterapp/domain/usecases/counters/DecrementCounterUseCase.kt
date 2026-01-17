package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.RootError
import io.droidevs.counterapp.domain.result.flatMap
import io.droidevs.counterapp.domain.result.flatMapSuspended
import io.droidevs.counterapp.domain.result.result
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.result.resultSuspendFromFlow
import io.droidevs.counterapp.domain.usecases.history.AddHistoryEventUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterDecrementStepUseCase
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

class DecrementCounterUseCase @Inject constructor(
    private val getCounterDecrementStepUseCase: GetCounterDecrementStepUseCase,
    private val updateCounterUseCase: UpdateCounterUseCase,
    private val addHistoryEventUseCase: AddHistoryEventUseCase,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(counter: Counter): Result<Unit, RootError> = withContext(dispatchers.io) {
        resultSuspendFromFlow {
            getCounterDecrementStepUseCase() // Returns Result<Int, PreferenceError>
                .combineSuspended { decrementStep -> // flatMap to change D type from Int to Unit
                    val oldValue = counter.currentCount
                    val newValue = oldValue - decrementStep
                    updateCounterUseCase(
                        UpdateCounterRequest(
                            counterId = counter.id,
                            newCount = newValue,
                            lastUpdatedAt = Instant.now()
                        )
                    ).combineSuspended { pair -> // Chaining with onSuccessWithResult for the subsequent operation
                        addHistoryEventUseCase(
                            HistoryEvent(
                                counterId = counter.id,
                                counterName = counter.name,
                                oldValue = oldValue,
                                newValue = newValue,
                                change = -decrementStep,
                            )
                        )
                    }
                }
        }
    }
}
