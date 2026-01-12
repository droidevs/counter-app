package io.droidevs.counterapp.repository


import io.droidevs.counterapp.data.entities.HistoryEventEntity
import io.droidevs.counterapp.data.entities.HistoryEventWithCounter
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeHistoryRepository(
    private val dummyData: DummyData
) : HistoryRepository {

    override fun getHistory(): Flow<List<HistoryEvent>> {
        return dummyData.historyEventsFlow.asStateFlow()
            .map { historyEvents ->
                val counters = dummyData.counters.associateBy { it.id }
                historyEvents.mapNotNull { historyEvent ->
                    counters[historyEvent.counterId]?.let {
                        HistoryEventWithCounter(
                            historyEvent = historyEvent,
                            counter = it
                        ).toDomain()
                    }
                }
            }
    }

    override suspend fun clearHistory() {
        dummyData.historyEvents.clear()
        dummyData.emitHistoryUpdate()
    }

    override suspend fun addHistoryEvent(event: HistoryEvent) {
        dummyData.historyEvents.add(event.toEntity())
        dummyData.emitHistoryUpdate()
    }
}
