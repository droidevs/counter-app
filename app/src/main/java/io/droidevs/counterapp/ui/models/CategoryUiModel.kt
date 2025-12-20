package io.droidevs.counterapp.ui.models

import io.droidevs.counterapp.domain.model.CategoryColor

data class CategoryUiModel(
    val id: String,
    val name: String,
    val color: CategoryColor = CategoryColor.default(),
    val countersCount: Int = 0
)