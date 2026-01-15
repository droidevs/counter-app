package io.droidevs.counterapp.ui.vm.events

sealed class CounterViewEvent {
    data class NavigateToCounterEdit(val counterId: String) : CounterViewEvent()
    object NavigateBack : CounterViewEvent()
}
