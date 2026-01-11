package io.droidevs.counterapp.ui.vm.actions

import io.droidevs.counterapp.ui.models.CounterUiModel

sealed class CounterListAction {
    data class IncrementCounter(val counter: CounterUiModel) : CounterListAction()
    data class DecrementCounter(val counter: CounterUiModel) : CounterListAction()
    data class VisibleItemsChanged(val items: Set<CounterUiModel>) : CounterListAction()
    object AddCounterClicked : CounterListAction()
    object FlushAllPendingReorders : CounterListAction()
}
