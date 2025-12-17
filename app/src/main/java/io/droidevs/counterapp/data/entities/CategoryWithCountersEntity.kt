package io.droidevs.counterapp.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithCountersEntity(

    @Embedded
    val category: CategoryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val counters : List<CounterEntity>
)