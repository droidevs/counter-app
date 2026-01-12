package io.droidevs.counterapp.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class HistoryEventWithCounter(
    @Embedded val historyEvent: HistoryEventEntity,
    @Relation(
        parentColumn = "counter_id",
        entityColumn = "id"
    )
    val counter: CounterEntity
)
