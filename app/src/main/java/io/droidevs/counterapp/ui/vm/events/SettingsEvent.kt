package io.droidevs.counterapp.ui.vm.events

sealed class SettingsEvent {
    object NavigateToHardware : SettingsEvent()
    object NavigateToDisplay : SettingsEvent()
    object NavigateToCounter : SettingsEvent()
    object NavigateToNotification : SettingsEvent()
    object NavigateToBackup : SettingsEvent()
    object NavigateToOther : SettingsEvent()
}
