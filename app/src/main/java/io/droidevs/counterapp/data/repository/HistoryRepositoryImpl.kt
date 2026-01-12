package io.droidevs.counterapp.data.repository

import io.droidevs.counterapp.data.dao.HistoryEventDao
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyEventDao: HistoryEventDao
) : HistoryRepository {

    override fun getHistory(): Flow<List<HistoryEvent>> {
        return historyEventDao.getHistory().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun clearHistory() {
        historyEventDao.clearHistory()
    }

    override suspend fun addHistoryEvent(event: HistoryEvent) {
        historyEventDao.addHistoryEvent(event.toEntity())
    }
}
