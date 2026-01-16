package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.RootError
import io.droidevs.counterapp.domain.result.flatMap
import io.droidevs.counterapp.domain.result.onSuccessWithResult
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.usecases.history.AddHistoryEventUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterIncrementStepUseCase
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

class IncrementCounterUseCase @Inject constructor(
    private val getCounterIncrementStepUseCase: GetCounterIncrementStepUseCase,
    private val updateCounterUseCase: UpdateCounterUseCase,
    private val addHistoryEventUseCase: AddHistoryEventUseCase,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(counter: Counter): Result<Unit, RootError> = withContext(dispatchers.io) {
        resultSuspend { // ResultBuilder<Unit, RootError> scope
            getCounterIncrementStepUseCase() // Returns Result<Int, PreferenceError>
                .flatMapSuspended { incrementStep -> // flatMap to change D type from Int to Unit
                    val oldValue = counter.currentCount
                    val newValue = oldValue + incrementStep
                    updateCounterUseCase(
                        UpdateCounterRequest(
                            counterId = counter.id,
                            newCount = newValue,
                            lastUpdatedAt = Instant.now()
                        )
                    ).flatMapSuspended { // Chaining with onSuccessWithResult for the subsequent operation
                        addHistoryEventUseCase(
                            HistoryEvent(
                                counterId = counter.id,
                                counterName = counter.name,
                                oldValue = oldValue,
                                newValue = newValue,
                                change = incrementStep,
                            )
                        )
                    }
                }
        }
    }
}
