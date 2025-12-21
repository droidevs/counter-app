package io.droidevs.counterapp.ui.models

data class CounterWithCategoryUiModel(
    val counter: CounterUiModel,
    val category: CategoryUiModel? = null
)