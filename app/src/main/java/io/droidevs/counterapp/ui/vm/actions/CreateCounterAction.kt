package io.droidevs.counterapp.ui.vm.actions

sealed class CreateCounterAction {
    data class NameChanged(val name: String) : CreateCounterAction()
    data class CanIncreaseChanged(val canIncrease: Boolean) : CreateCounterAction()
    data class CanDecreaseChanged(val canDecrease: Boolean) : CreateCounterAction()
    data class CategorySelected(val categoryId: String?) : CreateCounterAction()
    object SaveClicked : CreateCounterAction()
    data class InitialValueChanged(val value: Int): CreateCounterAction()

    // Per-counter behavior overrides
    data class UseDefaultBehaviorChanged(val value: Boolean) : CreateCounterAction()
    data class IncrementStepChanged(val value: String) : CreateCounterAction()
    data class DecrementStepChanged(val value: String) : CreateCounterAction()
    data class DefaultValueChanged(val value: String) : CreateCounterAction()
    data class MinValueChanged(val value: String) : CreateCounterAction()
    data class MaxValueChanged(val value: String) : CreateCounterAction()
}
