package io.droidevs.counterapp.domain

import io.droidevs.counterapp.data.CategoryEntity
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.ui.models.CounterCategoryUiModel

// Entity → Domain
fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        countersCount = countersCount
    )
}

// Domain → Entity
fun Category.toEntity(): CategoryEntity =
    CategoryEntity(id = id, name = name, countersCount = countersCount)


// Domain → UI
fun Category.toUiModel(): CounterCategoryUiModel {
    return CounterCategoryUiModel(
        id = id,
        name = name,
        countersCount = countersCount
    )
}

// UI → Domain (useful for create/edit later)
fun CounterCategoryUiModel.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        countersCount = countersCount
    )
}
