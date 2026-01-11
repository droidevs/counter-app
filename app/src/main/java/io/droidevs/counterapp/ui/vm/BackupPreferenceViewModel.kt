package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.BackupPreferenceUseCases
import io.droidevs.counterapp.ui.vm.actions.BackupPreferenceAction
import io.droidevs.counterapp.ui.vm.events.BackupPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.BackupPreferenceUiState
import io.droidevs.counterapp.ui.vm.mappers.toBackupPreferenceUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupPreferenceViewModel @Inject constructor(
    private val backup : BackupPreferenceUseCases
) : ViewModel() {

    private val _event = MutableSharedFlow<BackupPreferenceEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<BackupPreferenceEvent> = _event.asSharedFlow()

    val uiState: StateFlow<BackupPreferenceUiState> = combine(
        backup.getAutoBackup(),
        backup.getBackupInterval()
    ) { autoBackup, backupInterval ->
        Pair(autoBackup, backupInterval).toBackupPreferenceUiState()
    }
        .onStart { emit(BackupPreferenceUiState()) } // Initial default state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BackupPreferenceUiState()
        )

    fun onAction(action: BackupPreferenceAction) {
        when (action) {
            is BackupPreferenceAction.SetAutoBackup -> setAutoBackup(action.enabled)
            is BackupPreferenceAction.SetBackupInterval -> setBackupInterval(action.hours)
            BackupPreferenceAction.TriggerManualExport -> triggerManualExport()
        }
    }

    private fun setAutoBackup(enabled: Boolean) {
        viewModelScope.launch {
            backup.setAutoBackup(enabled)
            _event.emit(BackupPreferenceEvent.ShowMessage("Auto backup updated"))
        }
    }

    private fun setBackupInterval(hours: Long) {
        viewModelScope.launch {
            val coercedHours = hours.coerceIn(1L..720L)
            backup.setBackupInterval(coercedHours)
            _event.emit(BackupPreferenceEvent.ShowMessage("Backup interval updated"))
        }
    }

    private fun triggerManualExport() {
        viewModelScope.launch {
            _event.emit(BackupPreferenceEvent.ExportTriggered)
            _event.emit(BackupPreferenceEvent.ShowMessage("Manual export triggered (implementation pending)"))
        }
    }
}
