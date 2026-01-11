package io.droidevs.counterapp.ui.vm.states

data class BackupPreferenceUiState(
    val autoBackup: Boolean = false,
    val backupInterval: Long = 24L
)
