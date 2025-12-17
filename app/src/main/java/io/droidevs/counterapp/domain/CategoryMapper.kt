package io.droidevs.counterapp.domain

import io.droidevs.counterapp.domain.model.CounterCategory
import io.droidevs.counterapp.ui.CounterCategoryUiModel

// Domain → UI
fun CounterCategory.toUiModel(): CounterCategoryUiModel {
    return CounterCategoryUiModel(
        id = id,
        name = name,
        countersCount = countersCount
    )
}

// UI → Domain (useful for create/edit later)
fun CounterCategoryUiModel.toDomain(): CounterCategory {
    return CounterCategory(
        id = id,
        name = name,
        countersCount = countersCount
    )
}
