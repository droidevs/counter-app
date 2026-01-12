package io.droidevs.counterapp.domain.usecase

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.usecase.history.AddHistoryEventUseCase
import io.droidevs.counterapp.domain.usecases.counters.UpdateCounterUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterDecrementStepUseCase
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DecrementCounterUseCase @Inject constructor(
    private val getCounterDecrementStepUseCase: GetCounterDecrementStepUseCase,
    private val updateCounterUseCase: UpdateCounterUseCase,
    private val addHistoryEventUseCase: AddHistoryEventUseCase
) {
    suspend operator fun invoke(counter: Counter) {
        val oldValue = counter.value
        val decrementStep = getCounterDecrementStepUseCase().first()
        val newValue = oldValue - decrementStep
        updateCounterUseCase(
            UpdateCounterRequest(
                counterId = counter.id,
                newCount = newValue
            )
        )
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
