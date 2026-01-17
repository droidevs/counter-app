package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CategoryWithCountersUiModel
import io.droidevs.counterapp.ui.vm.states.CategoryViewUiState

fun CategoryWithCountersUiModel.toUiState(
    isLoading: Boolean = false,
    isError: Boolean = false
): CategoryViewUiState {
    return CategoryViewUiState(
        category = category,
        counters = counters,
        isLoading = isLoading,
        isError = isError,
        showEmptyState = counters.isEmpty()
    )
}
