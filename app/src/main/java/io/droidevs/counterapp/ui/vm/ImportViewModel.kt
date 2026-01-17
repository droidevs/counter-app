package io.droidevs.counterapp.ui.vm

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.onSuccess
import io.droidevs.counterapp.domain.services.FileImportService
import io.droidevs.counterapp.domain.usecases.importing.ImportUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage.Toast
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.ImportAction
import io.droidevs.counterapp.ui.vm.events.ImportEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val importUseCases: ImportUseCases,
    private val fileImportService: FileImportService,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _event = MutableSharedFlow<ImportEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<ImportEvent> = _event.asSharedFlow()

    fun onAction(action: ImportAction) {
        when (action) {
            is ImportAction.RequestImport -> requestImport()
            is ImportAction.Import -> import(action.fileUri)
        }
    }

    private fun requestImport() {
        viewModelScope.launch {
            val mimeTypes = fileImportService.getAvailableImportFormats().map { it.mimeType }.distinct().toTypedArray()
            _event.tryEmit(ImportEvent.ShowImportFileChooser(mimeTypes = mimeTypes))
        }
    }

    private fun import(fileUri: Uri) {
        viewModelScope.launch {
            val result: Result<Unit, *> = importUseCases.import(fileUri)
            result
                .onSuccess {
                    uiMessageDispatcher.dispatch(
                        Toast(
                            message = Message.Resource(R.string.counters_imported_successfully)
                        )
                    )
                }
                .onFailure { error ->
                    val resId = when (error) {
                        is io.droidevs.counterapp.domain.result.errors.FileError.UnsupportedFormat ->
                            R.string.import_unsupported_format

                        is io.droidevs.counterapp.domain.result.errors.FileError.ReadError ->
                            R.string.failed_to_import_counters

                        else -> R.string.failed_to_import_counters
                    }

                    uiMessageDispatcher.dispatch(
                        Toast(message = Message.Resource(resId))
                    )
                }
        }
    }
}
