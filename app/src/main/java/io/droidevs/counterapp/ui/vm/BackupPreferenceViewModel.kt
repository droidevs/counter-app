package io.droidevs.counterapp.ui.vm

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportResult
import io.droidevs.counterapp.domain.services.ImportResult
import io.droidevs.counterapp.domain.usecases.export.ExportUseCases
import io.droidevs.counterapp.domain.usecases.import.ImportUseCases
import io.droidevs.counterapp.domain.usecases.preference.BackupPreferenceUseCases
import io.droidevs.counterapp.ui.vm.actions.BackupPreferenceAction
import io.droidevs.counterapp.ui.vm.events.BackupPreferenceEvent
import io.droidevs.counterapp.ui.vm.mappers.toBackupPreferenceUiState
import io.droidevs.counterapp.ui.vm.states.BackupPreferenceUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupPreferenceViewModel @Inject constructor(
    private val backup: BackupPreferenceUseCases,
    private val export: ExportUseCases,
    private val import: ImportUseCases
) : ViewModel() {

    private val _event = MutableSharedFlow<BackupPreferenceEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<BackupPreferenceEvent> = _event.asSharedFlow()

    val uiState: StateFlow<BackupPreferenceUiState> = combine(
        backup.getAutoBackup(),
        backup.getBackupInterval()
    ) { autoBackup, backupInterval ->
        Pair(autoBackup, backupInterval).toBackupPreferenceUiState()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BackupPreferenceUiState()
    )

    fun onAction(action: BackupPreferenceAction) {
        when (action) {
            is BackupPreferenceAction.SetAutoBackup -> setAutoBackup(action.enabled)
            is BackupPreferenceAction.SetBackupInterval -> setBackupInterval(action.hours)
            BackupPreferenceAction.TriggerManualExport -> triggerManualExport()
            is BackupPreferenceAction.Export -> export(action.format)
            BackupPreferenceAction.TriggerManualImport -> triggerManualImport()
            is BackupPreferenceAction.Import -> import(action.fileUri)
        }
    }

    private fun setAutoBackup(enabled: Boolean) {
        viewModelScope.launch {
            backup.setAutoBackup(enabled)
            _event.tryEmit(BackupPreferenceEvent.ShowMessage("Auto backup updated"))
        }
    }

    private fun setBackupInterval(hours: Long) {
        viewModelScope.launch {
            val coercedHours = hours.coerceIn(1L..720L)
            backup.setBackupInterval(coercedHours)
            _event.tryEmit(BackupPreferenceEvent.ShowMessage("Backup interval updated"))
        }
    }

    private fun triggerManualExport() {
        viewModelScope.launch {
            val formats = export.getAvailableExportFormats()
            _event.tryEmit(BackupPreferenceEvent.ShowExportFormatDialog(formats))
        }
    }

    private fun export(format: ExportFormat) {
        viewModelScope.launch {
            when (val result = export.exportCounters(format)) {
                is ExportResult.Success -> _event.tryEmit(BackupPreferenceEvent.ShareExportFile(result.fileUri))
                is ExportResult.Error -> _event.tryEmit(BackupPreferenceEvent.ShowMessage(result.message))
                ExportResult.Cancelled -> Unit
            }
        }
    }

    private fun triggerManualImport() {
        viewModelScope.launch {
            _event.tryEmit(BackupPreferenceEvent.ShowImportFileChooser)
        }
    }

    private fun import(fileUri: Uri) {
        viewModelScope.launch {
            when (val result = import.importCounters(fileUri)) {
                is ImportResult.Success -> {
                    _event.tryEmit(BackupPreferenceEvent.ShowMessage("Counters imported successfully"))
                }
                is ImportResult.Error -> {
                    _event.tryEmit(BackupPreferenceEvent.ShowMessage(result.message))
                }
                ImportResult.Cancelled -> Unit
            }
        }
    }
}
