package io.droidevs.counterapp.ui.vm.actions

sealed class CounterEditAction {
    data class UpdateName(val name: String) : CounterEditAction()
    data class UpdateCurrentCount(val count: Int) : CounterEditAction()
    data class SetCanIncrease(val canIncrease: Boolean) : CounterEditAction()
    data class SetCanDecrease(val canDecrease: Boolean) : CounterEditAction()

    data class UseDefaultBehaviorChanged(val value: Boolean) : CounterEditAction()
    data class IncrementStepChanged(val value: String) : CounterEditAction()
    data class DecrementStepChanged(val value: String) : CounterEditAction()
    data class DefaultValueChanged(val value: String) : CounterEditAction()
    data class MinValueChanged(val value: String) : CounterEditAction()
    data class MaxValueChanged(val value: String) : CounterEditAction()

    object SaveClicked : CounterEditAction()
}
