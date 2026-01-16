package io.droidevs.counterapp.domain.usecases.history

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(): Result<Unit, DatabaseError> = withContext(dispatchers.io) {
        historyRepository.clearHistory()
    }
}
