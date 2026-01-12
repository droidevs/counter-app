package io.droidevs.counterapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "history_events",
    foreignKeys = [
        ForeignKey(
            entity = CounterEntity::class,
            parentColumns = ["id"],
            childColumns = ["counter_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("counter_id")]
)
data class HistoryEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "counter_id")
    val counterId: String,
    @ColumnInfo(name = "old_value")
    val oldValue: Int,
    @ColumnInfo(name = "new_value")
    val newValue: Int,
    val change: Int,
    val timestamp: Instant
)
