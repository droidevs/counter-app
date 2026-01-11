package io.droidevs.counterapp.ui.vm.actions

import io.droidevs.counterapp.ui.models.CategoryUiModel

sealed class CreateCounterAction {
    data class NameChanged(val name: String) : CreateCounterAction()
    data class CanIncreaseChanged(val canIncrease: Boolean) : CreateCounterAction()
    data class CanDecreaseChanged(val canDecrease: Boolean) : CreateCounterAction()
    data class CategorySelected(val categoryId: String?) : CreateCounterAction()
    object SaveClicked : CreateCounterAction()
}
