package io.droidevs.counterapp.domain

import io.droidevs.counterapp.data.entities.CategoryWithCountersEntity
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.domain.model.CategoryWithCounters
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.models.CategoryWithCountersUiModel

fun CategoryWithCountersEntity.toDomain() =
    CategoryWithCounters(
        category = category.toDomain(),
        counters = counters
            .asSequence()
            .map { it.toDomain() }
            .sortedByDescending { it.orderAnchorAt ?: it.createdAt }
            .toList()
    )

fun CategoryWithCounters.toUiModel(
    formatter: DateFormatter
) : CategoryWithCountersUiModel {
    return CategoryWithCountersUiModel(
        category = category.toUiModel(formatter),
        counters = counters.map { it.toUiModel(formatter) }
    )
}