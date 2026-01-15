package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.BackupPreferenceUseCases
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
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
    private val uiMessageDispatcher: UiMessageDispatcher
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
        }
    }

    private fun setAutoBackup(enabled: Boolean) {
        viewModelScope.launch {
            backup.setAutoBackup(enabled)
            uiMessageDispatcher.dispatch(UiMessage.Toast("Auto backup updated"))
        }
    }

    private fun setBackupInterval(hours: Long) {
        viewModelScope.launch {
            val coercedHours = hours.coerceIn(1L..720L)
            backup.setBackupInterval(coercedHours)
            uiMessageDispatcher.dispatch(UiMessage.Toast("Backup interval updated"))
        }
    }
}
