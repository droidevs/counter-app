package io.droidevs.counterapp.ui.vm.events

sealed class BackupPreferenceEvent {
    data class ShowMessage(val message: String) : BackupPreferenceEvent()
}