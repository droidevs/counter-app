package io.droidevs.counterapp.domain

import io.droidevs.counterapp.data.entities.HistoryEventEntity
import io.droidevs.counterapp.domain.model.HistoryEvent

fun HistoryEvent.toEntity(): HistoryEventEntity {
    return HistoryEventEntity(
        id = id,
        counterId = counterId,
        oldValue = oldValue,
        newValue = newValue,
        change = change,
        timestamp = timestamp
    )
}
