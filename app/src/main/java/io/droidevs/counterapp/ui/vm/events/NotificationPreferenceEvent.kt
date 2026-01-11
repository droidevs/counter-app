package io.droidevs.counterapp.ui.vm.events

sealed class NotificationPreferenceEvent {
    data class ShowMessage(val message: String) : NotificationPreferenceEvent()
}
