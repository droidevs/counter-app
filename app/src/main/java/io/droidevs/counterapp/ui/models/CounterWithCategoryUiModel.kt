package io.droidevs.counterapp.ui.models

data class CounterWithCategoryUiModel(
    val counter: CounterSnapshot,
    val category: CategoryUiModel? = null
)