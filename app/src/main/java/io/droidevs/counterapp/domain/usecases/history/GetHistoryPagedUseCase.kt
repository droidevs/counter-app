package io.droidevs.counterapp.domain.usecases.history

import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetHistoryPagedUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val dispatchers: DispatcherProvider
) {
    operator fun invoke(pageNumber: Int, pageSize: Int): Flow<Result<List<HistoryEvent>, DatabaseError>> =
        historyRepository.getHistoryPaged(pageNumber, pageSize).flowOn(dispatchers.io)
}

