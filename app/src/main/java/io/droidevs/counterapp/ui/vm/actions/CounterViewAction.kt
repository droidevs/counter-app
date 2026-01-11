package io.droidevs.counterapp.ui.vm.actions

sealed class CounterViewAction {
    object IncrementCounter : CounterViewAction()
    object DecrementCounter : CounterViewAction()
    object ResetCounter : CounterViewAction()
    object DeleteCounter : CounterViewAction()
    object EditCounterClicked : CounterViewAction()
}
