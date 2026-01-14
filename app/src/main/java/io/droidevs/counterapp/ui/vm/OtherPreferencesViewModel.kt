package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportResult
import io.droidevs.counterapp.domain.usecases.export.ExportUseCases
import io.droidevs.counterapp.domain.usecases.counters.RemoveAllCountersUseCase
import io.droidevs.counterapp.ui.vm.actions.OtherPreferencesAction
import io.droidevs.counterapp.ui.vm.events.OtherPreferencesEvent
import io.droidevs.counterapp.ui.vm.states.OtherPreferencesUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherPreferencesViewModel @Inject constructor(
    private val removeAllCountersUseCase: RemoveAllCountersUseCase,
    private val exportUseCases: ExportUseCases
) : ViewModel() {

    private val _event = Channel<OtherPreferencesEvent>(Channel.BUFFERED)
    val event: Flow<OtherPreferencesEvent> = _event.receiveAsFlow()

    private val _state = MutableStateFlow(OtherPreferencesUiState())
    val state: StateFlow<OtherPreferencesUiState> = _state.asStateFlow()

    fun onAction(action: OtherPreferencesAction) {
        when (action) {
            OtherPreferencesAction.RemoveCounters -> showRemoveConfirmation()
            OtherPreferencesAction.ExportRequest -> showExportFormatDialog()
            is OtherPreferencesAction.Export -> export(action.format)
            is OtherPreferencesAction.ConfirmRemoveCounters -> removeAllCounters()
        }
    }

    private fun showRemoveConfirmation() {
        viewModelScope.launch {
            _event.send(OtherPreferencesEvent.ShowRemoveConfirmation)
        }
    }

    private fun removeAllCounters() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                removeAllCountersUseCase()
                _state.value = _state.value.copy(isLoading = false)
                _event.send(OtherPreferencesEvent.RemoveSuccess)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
                _event.send(OtherPreferencesEvent.Error("Failed to remove counters: ${e.message}"))
            }
        }
    }

    private fun showExportFormatDialog() {
        viewModelScope.launch {
            val formats = exportUseCases.getAvailableExportFormats()
            _event.send(OtherPreferencesEvent.ShowExportFormatDialog(formats))
        }
    }

    private fun export(format: ExportFormat) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = exportUseCases.export(format)) {
                is ExportResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _event.trySend(OtherPreferencesEvent.ShareExportFile(result.fileUri))
                }
                is ExportResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false)
                    _event.trySend(OtherPreferencesEvent.Error(result.message))
                }
                ExportResult.Cancelled -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }
}
