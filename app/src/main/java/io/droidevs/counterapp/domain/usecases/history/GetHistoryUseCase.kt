package io.droidevs.counterapp.domain.usecases.history

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(): Flow<List<HistoryEvent>> = historyRepository.getHistory().flowOn(dispatchers.io)
}
