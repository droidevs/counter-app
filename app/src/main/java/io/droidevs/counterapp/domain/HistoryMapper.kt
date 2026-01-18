package io.droidevs.counterapp.domain

import io.droidevs.counterapp.data.entities.HistoryEventEntity
import io.droidevs.counterapp.domain.model.HistoryEvent
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.models.HistoryUiModel
import java.util.Date

fun HistoryEvent.toEntity(): HistoryEventEntity {
    return HistoryEventEntity(
        id = id.let { if (it.toInt() == -1) 0L else it } ?: 0L,
        counterId = counterId,
        oldValue = oldValue,
        newValue = newValue,
        change = change,
        timestamp = timestamp
    )
}

fun HistoryEvent.toUiModel(
    formatter : DateFormatter
): HistoryUiModel {
    return HistoryUiModel(
        id = this.id,
        counterId = this.counterId,
        counterName = this.counterName,
        oldValue = this.oldValue,
        newValue = this.newValue,
        change = this.change,
        createdTime = timestamp?.let { formatter.format(it) }
    )
}
