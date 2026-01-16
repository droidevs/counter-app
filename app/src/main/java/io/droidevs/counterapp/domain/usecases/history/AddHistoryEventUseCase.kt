package io.droidevs.counterapp.domain.usecases.history

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddHistoryEventUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(event: HistoryEvent) = withContext(dispatchers.io) {
        historyRepository.addHistoryEvent(event)
    }
}
