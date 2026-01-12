package io.droidevs.counterapp.domain.usecase.history

import io.droidevs.counterapp.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(): Flow<List<io.droidevs.counterapp.domain.model.HistoryEvent>> {
        return historyRepository.getHistory()
    }
}
