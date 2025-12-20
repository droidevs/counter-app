package io.droidevs.counterapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "color")
    val color : Int,
    @ColumnInfo(name = "counters_count")
    val countersCount: Int,
    @ColumnInfo(name = "is_system")
    val isSystem: Boolean = false
)