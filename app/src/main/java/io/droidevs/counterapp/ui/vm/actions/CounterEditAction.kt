package io.droidevs.counterapp.ui.vm.actions

sealed class CounterEditAction {
    data class UpdateName(val name: String) : CounterEditAction()
    data class UpdateCurrentCount(val count: Int) : CounterEditAction()
    data class SetCanIncrease(val canIncrease: Boolean) : CounterEditAction()
    data class SetCanDecrease(val canDecrease: Boolean) : CounterEditAction()
    object SaveClicked : CounterEditAction()
}
