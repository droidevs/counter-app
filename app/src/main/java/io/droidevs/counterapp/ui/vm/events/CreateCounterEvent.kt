package io.droidevs.counterapp.ui.vm.events

sealed class CreateCounterEvent {
    data class CounterCreated(val name: String) : CreateCounterEvent()
    data class ShowMessage(val message: String) : CreateCounterEvent()
    object NavigateBack : CreateCounterEvent()
}
