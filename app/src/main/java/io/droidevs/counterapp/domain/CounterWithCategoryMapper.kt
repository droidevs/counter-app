package io.droidevs.counterapp.domain

import io.droidevs.counterapp.data.entities.CategoryEntity
import io.droidevs.counterapp.data.entities.CounterWithCategoryEntity
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.data.toEntity
import io.droidevs.counterapp.domain.model.CounterWithCategory
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel


fun CounterWithCategoryEntity.toDomainModel(): CounterWithCategory {
    return CounterWithCategory(
        counter = counter.toDomain(),
        category = category.firstOrNull()?.toDomain()
    )
}

fun CounterWithCategory.toEntity(): CounterWithCategoryEntity {
    return CounterWithCategoryEntity(
        counter = counter.toEntity(),
        category = listOf(category?.toEntity()) as List<CategoryEntity>
    )
}

fun CounterWithCategoryUiModel.toDomain() : CounterWithCategory {
    return CounterWithCategory(
        counter = counter.toDomain(),
        category = category?.toDomain()
    )
}

fun CounterWithCategory.toUiModel() : CounterWithCategoryUiModel {
    return CounterWithCategoryUiModel(
        counter = counter.toUiModel(),
        category = category?.toUiModel()
    )
}