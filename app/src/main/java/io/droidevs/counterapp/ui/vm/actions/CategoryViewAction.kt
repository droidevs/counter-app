package io.droidevs.counterapp.ui.vm.actions

import io.droidevs.counterapp.ui.models.CounterUiModel

sealed class CategoryViewAction {
    object AddCounterClicked : CategoryViewAction()
    object DeleteCategoryClicked : CategoryViewAction()
    data class SetCategoryId(val categoryId: String) : CategoryViewAction()

    data class IncrementCounter(val counter: CounterUiModel) : CategoryViewAction()
    data class DecrementCounter(val counter: CounterUiModel) : CategoryViewAction()
}
