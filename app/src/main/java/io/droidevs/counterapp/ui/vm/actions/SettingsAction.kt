package io.droidevs.counterapp.ui.vm.actions

sealed class SettingsAction {
    object HardwareClicked : SettingsAction()
    object DisplayClicked : SettingsAction()
    object CounterClicked : SettingsAction()
    object NotificationClicked : SettingsAction()
    object BackupClicked : SettingsAction()
    object OtherClicked : SettingsAction()
}
