package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import io.droidevs.counterapp.ui.vm.states.HomeUiState

fun Quadruple<Int, Int, Boolean, Boolean>.toHomeUiState(
    recentCounters: List<CounterWithCategoryUiModel>,
    categories: List<CategoryUiModel>,
    isError: Boolean = false
): HomeUiState {
    val isLoadingCounters = this.third
    val isLoadingCategories = this.fourth

    return HomeUiState(
        recentCounters = recentCounters,
        countersCount = this.first,
        categories = categories,
        categoriesCount = this.second,
        isLoadingCounters = isLoadingCounters,
        isLoadingCategories = isLoadingCategories,
        isLoading = isLoadingCounters || isLoadingCategories,
        isError = isError
    )
}
