package io.droidevs.counterapp.data.repository

import io.droidevs.counterapp.data.dao.HistoryEventDao
import io.droidevs.counterapp.data.repository.exceptions.flowRunCatchingDatabase
import io.droidevs.counterapp.data.repository.exceptions.runCatchingDatabaseResult
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyEventDao: HistoryEventDao
) : HistoryRepository {

    override fun getHistory(): Flow<Result<List<HistoryEvent>, DatabaseError>> = flowRunCatchingDatabase {
        historyEventDao.getAllEventsWithCounter().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun clearHistory(): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        historyEventDao.clearAllEvents()
    }

    override suspend fun addHistoryEvent(event: HistoryEvent): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        historyEventDao.insertEvent(event.toEntity())
    }
}
