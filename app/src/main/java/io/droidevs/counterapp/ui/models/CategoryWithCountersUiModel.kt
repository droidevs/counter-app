package io.droidevs.counterapp.ui.models

data class CategoryWithCountersUiModel(
    val category: CategoryUiModel,
    val counters: List<CounterUiModel>
)