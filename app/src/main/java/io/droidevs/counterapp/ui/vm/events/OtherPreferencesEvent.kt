package io.droidevs.counterapp.ui.vm.events

sealed class OtherPreferencesEvent {
    object ShowRemoveConfirmation : OtherPreferencesEvent()
    object RemoveSuccess : OtherPreferencesEvent()
    data class Error(val message: String) : OtherPreferencesEvent()
}