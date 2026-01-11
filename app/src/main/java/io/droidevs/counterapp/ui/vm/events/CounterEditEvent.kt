package io.droidevs.counterapp.ui.vm.events

sealed class CounterEditEvent {
    object CounterSaved : CounterEditEvent()
    data class ShowMessage(val message: String) : CounterEditEvent()
}
