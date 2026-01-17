package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.result.onFailureSuspend
import io.droidevs.counterapp.domain.result.onSuccessSuspend
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.usecases.preference.BackupPreferenceUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.BackupPreferenceAction
import io.droidevs.counterapp.ui.vm.events.BackupPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.BackupPreferenceUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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
    ) { autoBackupResult, backupIntervalResult ->
        // If any input is Failure, emit Failure so it can propagate to recoverWith.
        when {
            autoBackupResult is Result.Failure -> Result.Failure(autoBackupResult.error)
            backupIntervalResult is Result.Failure -> Result.Failure(backupIntervalResult.error)
            else -> {
                val autoBackup = autoBackupResult.dataOr { false }
                val interval = backupIntervalResult.dataOr { 24L }
                Result.Success(
                    BackupPreferenceUiState(
                        autoBackup = autoBackup,
                        backupInterval = interval,
                        error = false,
                        isLoading = false
                    )
                )
            }
        }
    }
        .recoverWith {
            Result.Success(
                BackupPreferenceUiState(
                    autoBackup = false,
                    backupInterval = 24L,
                    error = true,
                    isLoading = false
                )
            )
        }
        .map { (it as Result.Success).data }
        .onStart { emit(BackupPreferenceUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BackupPreferenceUiState(isLoading = true)
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
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.auto_backup_updated)
                        )
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.failed_to_update_auto_backup)
                        )
                    )
                }
        }
    }

    private fun setBackupInterval(hours: Long) {
        viewModelScope.launch {
            val coercedHours = hours.coerceIn(1L..720L)
            backup.setBackupInterval(coercedHours)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.backup_interval_updated)
                        )
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.failed_to_update_backup_interval)
                        )
                    )
                }
        }
    }
}
