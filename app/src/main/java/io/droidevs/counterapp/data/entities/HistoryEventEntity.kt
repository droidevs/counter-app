package io.droidevs.counterapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "history_events")
data class HistoryEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val counterId: String,
    val counterName: String,
    val oldValue: Int,
    val newValue: Int,
    val change: Int,
    val timestamp: Instant
)
