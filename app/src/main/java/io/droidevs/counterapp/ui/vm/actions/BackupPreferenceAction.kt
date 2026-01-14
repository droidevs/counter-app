package io.droidevs.counterapp.ui.vm.actions

sealed class BackupPreferenceAction {
    data class SetAutoBackup(val enabled: Boolean) : BackupPreferenceAction()
    data class SetBackupInterval(val hours: Long) : BackupPreferenceAction()
}