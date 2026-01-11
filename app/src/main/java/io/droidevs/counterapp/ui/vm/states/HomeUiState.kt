package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel

data class HomeUiState(
    val recentCounters: List<CounterWithCategoryUiModel> = emptyList(),
    val categories: List<CategoryUiModel> = emptyList(),
    val countersCount: Int = 0,
    val categoriesCount: Int = 0,
    val isLoadingCounters: Boolean = false,
    val isLoadingCategories: Boolean = false
)
