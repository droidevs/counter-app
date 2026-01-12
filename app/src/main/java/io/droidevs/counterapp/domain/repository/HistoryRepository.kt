package io.droidevs.counterapp.domain.repository

import io.droidevs.counterapp.domain.model.HistoryEvent
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getHistory(): Flow<List<HistoryEvent>>

    suspend fun clearHistory()

    suspend fun addHistoryEvent(event: HistoryEvent)
}
