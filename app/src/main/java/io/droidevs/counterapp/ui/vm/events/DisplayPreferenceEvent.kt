package io.droidevs.counterapp.ui.vm.events

sealed class DisplayPreferenceEvent {
    data class ShowMessage(val message: String) : DisplayPreferenceEvent()
}
