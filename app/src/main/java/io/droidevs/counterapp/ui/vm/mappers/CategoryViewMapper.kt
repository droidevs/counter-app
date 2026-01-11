package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.models.CategoryWithCountersUiModel
import io.droidevs.counterapp.ui.vm.states.CategoryViewUiState

fun CategoryWithCountersUiModel.toUiState(isLoading: Boolean = false): CategoryViewUiState {
    return CategoryViewUiState(
        category = category,
        counters = counters,
        isLoading = isLoading,
        showEmptyState = counters.isEmpty()
    )
}
