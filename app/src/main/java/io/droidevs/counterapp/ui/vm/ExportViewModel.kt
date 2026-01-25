package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.onSuccess
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.domain.services.ExportSuccessResult
import io.droidevs.counterapp.domain.usecases.export.ExportUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.ExportAction
import io.droidevs.counterapp.ui.vm.events.ExportEvent
import io.droidevs.counterapp.domain.result.errors.FileError
import io.droidevs.counterapp.util.TracingHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val exportUseCases: ExportUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val tracing: TracingHelper
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
            tracing.tracedSuspend("export_request_formats") {
                val formats = exportUseCases.getAvailableExportFormats()
                _event.tryEmit(ExportEvent.ShowExportFormatDialog(formats))
            }
        }
    }

    private fun export(format: ExportFormat) {
        viewModelScope.launch {
            tracing.tracedSuspend("export_do_export") {
                exportUseCases.export(format)
            }
                .onSuccess { success ->
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.export_success))
                    )
                    _event.tryEmit(ExportEvent.ShareExportFile(success.fileUri))
                }
                .onFailure { error ->
                    val resId = when (error) {
                        is FileError.UnsupportedFormat -> R.string.export_failed
                        is FileError.ReadError -> R.string.export_failed
                        is FileError.WriteError -> R.string.export_failed
                        is FileError.ShareError -> R.string.export_failed
                        else -> R.string.export_failed
                    }

                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = resId))
                    )
                }
        }
    }
}
