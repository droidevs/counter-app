package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import io.droidevs.counterapp.ui.vm.states.HomeUiState

fun Quadruple<Int, Int, Boolean, Boolean>.toHomeUiState(
    recentCounters : List<CounterWithCategoryUiModel>,
    categories : List<CategoryUiModel>
): HomeUiState {
    return HomeUiState(
        recentCounters = recentCounters,
        countersCount = this.first,
        categories = categories,
        categoriesCount = this.second,
        isLoadingCounters = this.third,
        isLoadingCategories = this.fourth
    )
}
