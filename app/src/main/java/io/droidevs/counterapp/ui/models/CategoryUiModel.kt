package io.droidevs.counterapp.ui.models

data class CategoryUiModel(
    val id: String,
    val name: String,
    val countersCount: Int = 0
)