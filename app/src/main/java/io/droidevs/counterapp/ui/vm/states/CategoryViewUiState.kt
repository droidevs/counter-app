package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel

data class CategoryViewUiState(
    val category: CategoryUiModel? = null,
    val counters: List<CounterWithCategoryUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val showEmptyState: Boolean = false
)
