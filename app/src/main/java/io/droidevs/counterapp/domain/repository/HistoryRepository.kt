package io.droidevs.counterapp.domain.repository

import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getHistory(): Flow<Result<List<HistoryEvent>, DatabaseError>>

    suspend fun clearHistory(): Result<Unit, DatabaseError>

    suspend fun addHistoryEvent(event: HistoryEvent): Result<Unit, DatabaseError>
}
