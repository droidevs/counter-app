package io.droidevs.counterapp.ui.vm.states

data class BackupPreferenceUiState(
    val isLoading: Boolean = false,
    val autoBackup: Boolean = false,
    val backupInterval: Long = 24L,
    val error: Boolean = false
)
