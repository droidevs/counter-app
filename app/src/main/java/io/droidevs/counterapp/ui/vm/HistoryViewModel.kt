package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.usecases.history.HistoryUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.UiMessage.Toast
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.models.toUiModel
import io.droidevs.counterapp.ui.vm.actions.HistoryViewAction
import io.droidevs.counterapp.ui.vm.events.HistoryViewEvent
import io.droidevs.counterapp.ui.vm.states.HistoryViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCases: HistoryUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryViewState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HistoryViewEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        getHistory()
    }

    fun onAction(action: HistoryViewAction) {
        when (action) {
            is HistoryViewAction.ClearHistory -> clearHistory()
        }
    }

    private fun getHistory() {
        historyUseCases.getHistoryUseCase()
            .onEach { result ->
                _uiState.update { state ->
                    state.copy(
                        history = result.map { it.toUiModel() },
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun clearHistory() {
        viewModelScope.launch {
            historyUseCases.clearHistoryUseCase()
            uiMessageDispatcher.dispatch(
                Toast(
                    message = Message.Resource(R.string.history_cleared)
                )
            )

        }
    }
}
