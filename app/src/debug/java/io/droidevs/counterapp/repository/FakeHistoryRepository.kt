package io.droidevs.counterapp.repository

import io.droidevs.counterapp.data.entities.HistoryEventWithCounter
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeHistoryRepository(
    private val dummyData: DummyData
) : HistoryRepository {

    override fun getHistory(): Flow<Result<List<HistoryEvent>, DatabaseError>> {
        return dummyData.historyEventsFlow.asStateFlow()
            .map { historyEvents ->
                val counters = dummyData.counters.associateBy { it.id }
                val history = historyEvents.mapNotNull { historyEvent ->
                    counters[historyEvent.counterId]?.let {
                        HistoryEventWithCounter(
                            historyEvent = historyEvent,
                            counter = it
                        ).toDomain()
                    }
                }
                Result.Success(history)
            }
    }

    override suspend fun clearHistory(): Result<Unit, DatabaseError> {
        dummyData.historyEvents.clear()
        dummyData.emitHistoryUpdate()
        return Result.Success(Unit)
    }

    override suspend fun addHistoryEvent(event: HistoryEvent): Result<Unit, DatabaseError> {
        dummyData.historyEvents.add(event.toEntity())
        dummyData.emitHistoryUpdate()
        return Result.Success(Unit)
    }
}
