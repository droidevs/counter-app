package io.droidevs.counterapp.ui.vm.actions

import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CounterUiModel

sealed class HomeAction {
    data class IncrementCounter(val counter: CounterUiModel) : HomeAction()
    data class DecrementCounter(val counter: CounterUiModel) : HomeAction()
    data class CounterClicked(val counter: CounterUiModel) : HomeAction()
    object AddCounterClicked : HomeAction()
    data class CategoryClicked(val category: CategoryUiModel) : HomeAction()
    object AddCategoryClicked : HomeAction()
    object ViewAllCountersClicked : HomeAction()
    object ViewAllCategoriesClicked : HomeAction()
}
