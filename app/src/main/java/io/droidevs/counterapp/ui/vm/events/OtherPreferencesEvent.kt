package io.droidevs.counterapp.ui.vm.events

sealed class OtherPreferencesEvent {
    object ShowRemoveConfirmation : OtherPreferencesEvent()
}
