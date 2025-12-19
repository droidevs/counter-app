package io.droidevs.counterapp.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CounterWithCategoryEntity(
    @Embedded val counter: CounterEntity,

    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)