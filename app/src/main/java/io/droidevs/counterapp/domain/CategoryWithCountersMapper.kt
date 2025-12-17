package io.droidevs.counterapp.domain

import io.droidevs.counterapp.data.CategoryWithCountersEntity
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.domain.model.CategoryWithCounters
import io.droidevs.counterapp.ui.models.CategoryWithCountersUiModel

fun CategoryWithCountersEntity.toDomain() =
    CategoryWithCounters(
        category = category.toDomain(),
        counters = counters.map { it.toDomain() }
    )

fun CategoryWithCounters.toUiModel() : CategoryWithCountersUiModel {
    return CategoryWithCountersUiModel(
        categoryId = category.id,
        categoryName = category.name,
        counters = counters.map { it.toSnapshot() }
    )
}