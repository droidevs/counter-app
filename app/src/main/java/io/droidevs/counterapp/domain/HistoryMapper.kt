package io.droidevs.counterapp.domain

import io.droidevs.counterapp.data.entities.HistoryEventEntity
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.models.HistoryUiModel

fun HistoryEvent.toEntity(): HistoryEventEntity {
    // Important:
    // - For new events, we must pass id=0 so Room auto-generates an id.
    // - If we pass a fixed id (like 0) with IGNORE, inserts can be skipped.
    // - If we pass an existing id (>0), this represents an update/merge target.
    val entityId = if (id <= 0L) 0L else id

    return HistoryEventEntity(
        id = entityId,
        counterId = counterId,
        oldValue = oldValue,
        newValue = newValue,
        change = change,
        timestamp = timestamp
    )
}

fun HistoryEvent.toUiModel(
    formatter: DateFormatter
): HistoryUiModel {
    return HistoryUiModel(
        id = this.id,
        counterId = this.counterId,
        counterName = this.counterName,
        oldValue = this.oldValue,
        newValue = this.newValue,
        change = this.change,
        createdTime = formatter.format(timestamp)
    )
}
