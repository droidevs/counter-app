package io.droidevs.counterapp.domain

import io.droidevs.counterapp.data.entities.CategoryEntity
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryColor
import io.droidevs.counterapp.ui.models.CategoryUiModel

// Entity → Domain
fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        color = CategoryColor(color),
        countersCount = countersCount,
        isSystem = isSystem
    )
}

// Domain → Entity
fun Category.toEntity(): CategoryEntity =
    CategoryEntity(
        id = id, name = name, countersCount = countersCount,
        color = color.colorInt,
        isSystem = isSystem
    )


// Domain → UI
fun Category.toUiModel(): CategoryUiModel {
    return CategoryUiModel(
        id = id,
        name = name,
        countersCount = countersCount
    )
}

// UI → Domain (useful for create/edit later)
fun CategoryUiModel.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        color = color,
        countersCount = countersCount
    )
}
