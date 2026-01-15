package io.droidevs.counterapp.ui.vm.events

sealed class CreateCounterEvent {
    object NavigateBack : CreateCounterEvent()
}
