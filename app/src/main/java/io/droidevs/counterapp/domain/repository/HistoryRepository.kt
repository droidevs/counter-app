package io.droidevs.counterapp.domain.repository

import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface HistoryRepository {
    /**
     * Deprecated: Use getHistoryPaged instead.
     */
    @Deprecated("Use getHistoryPaged(pageNumber, pageSize) for pagination support.")
    fun getHistory(): Flow<Result<List<HistoryEvent>, DatabaseError>>

    /**
     * New paginated method for history events.
     */
    fun getHistoryPaged(pageNumber: Int, pageSize: Int): Flow<Result<List<HistoryEvent>, DatabaseError>>

    suspend fun clearHistory(): Result<Unit, DatabaseError>

    suspend fun addHistoryEvent(event: HistoryEvent): Result<Unit, DatabaseError>

    /** Latest stored history event for this counter (for merge logic). */
    suspend fun getLastEventForCounter(counterId: String): Result<HistoryEvent?, DatabaseError>

    /** Update an existing history row (used by merge logic). */
    suspend fun updateHistoryEvent(
        id: Long,
        oldValue: Int,
        newValue: Int,
        change: Int,
        timestamp: Instant
    ): Result<Unit, DatabaseError>
}
