package io.droidevs.counterapp.domain.usecases.counters

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.HistoryEvent
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
    suspend operator fun invoke(counter: Counter) = withContext(dispatchers.io) {
        val oldValue = counter.currentCount
        val incrementStep = getCounterIncrementStepUseCase().first()
        val newValue = oldValue + incrementStep
        updateCounterUseCase(
            UpdateCounterRequest(
                counterId = counter.id,
                newCount = newValue,
                lastUpdatedAt = Instant.now()
            )
        )
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
