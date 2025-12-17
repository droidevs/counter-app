package io.droidevs.counterapp.ui.models

data class CategoryWithCountersUiModel(
    val categoryId: String,
    val categoryName: String,
    val counters: List<CounterSnapshot>
) {
    val countersCount: Int
        get() = counters.size
}