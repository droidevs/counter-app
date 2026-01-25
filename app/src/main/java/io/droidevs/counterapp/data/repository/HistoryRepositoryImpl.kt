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
import java.time.Instant
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyEventDao: HistoryEventDao
) : HistoryRepository {
    /**
     * Deprecated: Use getHistoryPaged instead.
     */
    @Deprecated("Use getHistoryPaged(pageNumber, pageSize) for pagination support.")
    override fun getHistory(): Flow<Result<List<HistoryEvent>, DatabaseError>> = flowRunCatchingDatabase {
        historyEventDao.getAllEventsWithCounter().map { list ->
            list.map { it.toDomain() }
        }
    }

    /**
     * New paginated method for history events.
     */
    override fun getHistoryPaged(pageNumber: Int, pageSize: Int): Flow<Result<List<HistoryEvent>, DatabaseError>> = flowRunCatchingDatabase {
        historyEventDao.getAllEventsWithCounterPaged(pageNumber, pageSize).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun clearHistory(): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        historyEventDao.clearAllEvents()
    }

    override suspend fun addHistoryEvent(event: HistoryEvent): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        historyEventDao.insertEvent(event.toEntity())
    }

    override suspend fun getLastEventForCounter(counterId: String): Result<HistoryEvent?, DatabaseError> =
        runCatchingDatabaseResult {
            historyEventDao.getLastEventForCounter(counterId)?.let { entity ->
                // Note: HistoryEvent Domain includes counterName; for merge logic we only need values.
                HistoryEvent(
                    id = entity.id,
                    counterId = entity.counterId,
                    counterName = "", // not needed for merge/update
                    oldValue = entity.oldValue,
                    newValue = entity.newValue,
                    change = entity.change,
                    timestamp = entity.timestamp
                )
            }
        }

    override suspend fun updateHistoryEvent(
        id: Long,
        oldValue: Int,
        newValue: Int,
        change: Int,
        timestamp: Instant
    ): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        historyEventDao.updateEvent(
            id = id,
            oldValue = oldValue,
            newValue = newValue,
            change = change,
            timestamp = timestamp
        )
    }
}
