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
    indices = [Index("category_id"), Index(value = ["kay"], unique = true)],
)
data class CounterEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "kay")
    val kay: String? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "current_count")
    val currentCount: Int,
    @ColumnInfo(name = "can_increment")
    val canIncrement: Boolean,
    @ColumnInfo(name = "can_decrement")
    val canDecrement: Boolean,

    @ColumnInfo(name = "increment_step")
    val incrementStep: Int? = null,
    @ColumnInfo(name = "decrement_step")
    val decrementStep: Int? = null,

    @ColumnInfo(name = "min_value")
    val minValue: Int? = null,
    @ColumnInfo(name = "max_value")
    val maxValue: Int? = null,

    @ColumnInfo(name = "default_value")
    val defaultValue: Int? = null,

    /** If true, use global preferences (if available) as fallback. */
    @ColumnInfo(name = "use_default_behavior")
    val useDefaultBehavior: Boolean = true,

    @ColumnInfo(name = "category_id")
    val categoryId: String?,

    @ColumnInfo(name = "is_system")
    val isSystem: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "last_updated_at")
    val lastUpdatedAt: Instant? = null,

    @ColumnInfo(name = "order_anchor_at")
    val orderAnchorAt: Instant? = null
)