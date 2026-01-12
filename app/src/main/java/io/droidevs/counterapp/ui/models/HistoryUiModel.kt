package io.droidevs.counterapp.ui.models

import io.droidevs.counterapp.domain.model.HistoryEvent
import java.time.Instant

data class HistoryUiModel(
    val id: Long,
    val counterId: String,
    val counterName: String,
    val oldValue: Int,
    val newValue: Int,
    val change: Int,
    val timestamp: Instant
)

fun HistoryEvent.toUiModel(): HistoryUiModel {
    return HistoryUiModel(
        id = this.id,
        counterId = this.counterId,
        counterName = this.counterName,
        oldValue = this.oldValue,
        newValue = this.newValue,
        change = this.change,
        timestamp = this.timestamp
    )
}
