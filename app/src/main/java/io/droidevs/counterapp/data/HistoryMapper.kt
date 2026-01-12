package io.droidevs.counterapp.data

import io.droidevs.counterapp.data.entities.HistoryEventWithCounter
import io.droidevs.counterapp.domain.model.HistoryEvent

fun HistoryEventWithCounter.toDomain(): HistoryEvent {
    return HistoryEvent(
        id = this.historyEvent.id,
        counterId = this.historyEvent.counterId,
        counterName = this.counter.name,
        oldValue = this.historyEvent.oldValue,
        newValue = this.historyEvent.newValue,
        change = this.historyEvent.change,
        timestamp = this.historyEvent.timestamp
    )
}
