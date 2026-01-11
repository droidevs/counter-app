package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import io.droidevs.counterapp.ui.vm.states.HomeUiState

fun Sixfold<List<CounterWithCategoryUiModel>, Int, List<CategoryUiModel>, Int, Boolean, Boolean>.toHomeUiState(): HomeUiState {
    return HomeUiState(
        recentCounters = this.first,
        countersCount = this.second,
        categories = this.third,
        categoriesCount = this.fourth,
        isLoadingCounters = this.fifth,
        isLoadingCategories = this.sixth
    )
}
