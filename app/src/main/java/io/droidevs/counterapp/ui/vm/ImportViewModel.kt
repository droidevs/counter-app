package io.droidevs.counterapp.ui.vm

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.services.ImportResult
import io.droidevs.counterapp.domain.usecases.importing.ImportUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
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
            _event.tryEmit(ImportEvent.ShowImportFileChooser)
        }
    }

    private fun import(fileUri: Uri) {
        viewModelScope.launch {
            when (val result = importUseCases.import(fileUri)) {
                is ImportResult.Success -> {
                    uiMessageDispatcher.dispatch(
                        Toast(
                            message = Message.Resource(R.string.counters_imported_successfully)
                        )
                    )


                }
                is ImportResult.Error -> {
                    uiMessageDispatcher.dispatch(Toast(message = Message.Text(result.message)))
                }
                ImportResult.Cancelled -> Unit
            }
        }
    }
}
