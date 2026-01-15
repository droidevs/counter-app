package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportResult
import io.droidevs.counterapp.domain.usecases.export.ExportUseCases
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.ExportAction
import io.droidevs.counterapp.ui.vm.events.ExportEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val exportUseCases: ExportUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _event = MutableSharedFlow<ExportEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<ExportEvent> = _event.asSharedFlow()

    fun onAction(action: ExportAction) {
        when (action) {
            is ExportAction.RequestExport -> requestExport()
            is ExportAction.Export -> export(action.format)
        }
    }

    private fun requestExport() {
        viewModelScope.launch {
            val formats = exportUseCases.getAvailableExportFormats()
            _event.tryEmit(ExportEvent.ShowExportFormatDialog(formats))
        }
    }

    private fun export(format: ExportFormat) {
        viewModelScope.launch {
            when (val result = exportUseCases.export(format)) {
                is ExportResult.Success -> _event.tryEmit(ExportEvent.ShareExportFile(result.fileUri))
                is ExportResult.Error -> uiMessageDispatcher.emit(UiMessage.Snackbar(result.message))
                ExportResult.Cancelled -> Unit
            }
        }
    }
}
