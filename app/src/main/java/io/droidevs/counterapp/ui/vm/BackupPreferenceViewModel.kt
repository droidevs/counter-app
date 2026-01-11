package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.BackupPreferenceUseCases
import io.droidevs.counterapp.ui.vm.actions.BackupPreferenceAction
import io.droidevs.counterapp.ui.vm.events.BackupPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.BackupPreferenceUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupPreferenceViewModel @Inject constructor(
    private val backup : BackupPreferenceUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(BackupPreferenceUiState())
    val uiState: StateFlow<BackupPreferenceUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<BackupPreferenceEvent>()
    val event: SharedFlow<BackupPreferenceEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            backup.getAutoBackup()
                .onStart { emit(false) } // Default initial value
                .collect { value ->
                    _uiState.update { it.copy(autoBackup = value) }
                }
        }

        viewModelScope.launch {
            backup.getBackupInterval()
                .onStart { emit(24L) } // Default initial value
                .collect { value ->
                    _uiState.update { it.copy(backupInterval = value) }
                }
        }
    }

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
            _uiState.update { it.copy(autoBackup = enabled) }
        }
    }

    private fun setBackupInterval(hours: Long) {
        viewModelScope.launch {
            val coercedHours = hours.coerceIn(1L..720L)
            backup.setBackupInterval(coercedHours)
            _uiState.update { it.copy(backupInterval = coercedHours) }
        }
    }

    private fun triggerManualExport() {
        viewModelScope.launch {
            _event.emit(BackupPreferenceEvent.ExportTriggered)
            _event.emit(BackupPreferenceEvent.ShowMessage("Manual export triggered (implementation pending)"))
        }
    }
}
