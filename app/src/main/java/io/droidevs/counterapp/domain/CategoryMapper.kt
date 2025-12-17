package io.droidevs.counterapp.domain

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.ui.CounterCategoryUiModel

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
