package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import io.droidevs.counterapp.ui.vm.states.CounterListUiState

fun List<CounterWithCategoryUiModel>.toUiState(
    isLoading: Boolean = false,
    isError: Boolean = false
): CounterListUiState {
    return CounterListUiState(
        counters = this,
        isLoading = isLoading,
        isError = isError,
        showEmptyState = this.isEmpty()
    )
}
