package io.droidevs.counterapp.domain.usecases.history

import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import javax.inject.Inject

class AddHistoryEventUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(event: HistoryEvent) {
        historyRepository.addHistoryEvent(event)
    }
}
