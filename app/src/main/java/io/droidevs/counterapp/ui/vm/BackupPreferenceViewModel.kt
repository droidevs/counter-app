package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.BackupPreferenceUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupPreferenceViewModel @Inject constructor(
    private val backup : BackupPreferenceUseCases
) : ViewModel() {

    val autoBackup: StateFlow<Boolean> = backup.getAutoBackup()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val backupInterval: StateFlow<Long> = backup.getBackupInterval()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 24L)

    fun setAutoBackup(enabled: Boolean) {
        viewModelScope.launch {
            backup.setAutoBackup(enabled)
        }
    }

    fun setBackupInterval(hours: Long) {
        viewModelScope.launch {
            backup.setBackupInterval(hours.coerceIn(1L..720L)) // reasonable limits
        }
    }

    fun triggerManualExport() {
        // Implement export logic (e.g. create JSON/zip → SAF share sheet)
        viewModelScope.launch {
            // ... your export code ...
        }
    }
}