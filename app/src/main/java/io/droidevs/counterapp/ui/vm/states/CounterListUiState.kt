package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel

data class CounterListUiState(
    val counters: List<CounterWithCategoryUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val showEmptyState: Boolean = false
)
