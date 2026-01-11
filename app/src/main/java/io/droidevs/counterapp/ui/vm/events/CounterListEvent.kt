package io.droidevs.counterapp.ui.vm.events


sealed class CounterListEvent {
    data class NavigateToCounterView(val counterId: String) : CounterListEvent()
    object NavigateToCreateCounter : CounterListEvent()
    data class ShowMessage(val message: String) : CounterListEvent()
}
