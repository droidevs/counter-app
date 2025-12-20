package io.droidevs.counterapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "counters",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.Companion.SET_NULL
        )
    ],
    indices = [Index("category_id")]
)
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

    @ColumnInfo(name = "category_id")
    val categoryId: String? = null,
    @ColumnInfo(name = "is_system")
    val isSystem: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "last_updated_at")
    val lastUpdatedAt: Instant
)