package io.droidevs.counterapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "counters")
data class CounterEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "current_count")
    val currentCount: Int,
    @ColumnInfo(name = "can_increment")
    val canIncrement: Boolean,
    @ColumnInfo(name = "can_decrement")
    val canDecrement: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "last_updated_at")
    val lastUpdatedAt: Instant
)
