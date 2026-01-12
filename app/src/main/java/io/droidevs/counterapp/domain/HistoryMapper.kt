package io.droidevs.counterapp.domain

import io.droidevs.counterapp.data.entities.HistoryEventEntity
import io.droidevs.counterapp.domain.model.HistoryEvent

fun HistoryEventEntity.toDomain(): HistoryEvent {
    return HistoryEvent(
        id = id,
        counterId = counterId,
        counterName = counterName,
        oldValue = oldValue,
        newValue = newValue,
        change = change,
        timestamp = timestamp
    )
}

fun HistoryEvent.toEntity(): HistoryEventEntity {
    return HistoryEventEntity(
        id = id,
        counterId = counterId,
        counterName = counterName,
        oldValue = oldValue,
        newValue = newValue,
        change = change,
        timestamp = timestamp
    )
}
