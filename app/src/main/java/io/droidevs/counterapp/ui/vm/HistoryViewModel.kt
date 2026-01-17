package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.mapResult
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.onSuccess
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.history.HistoryUseCases
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage.Toast
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.HistoryViewAction
import io.droidevs.counterapp.ui.vm.events.HistoryViewEvent
import io.droidevs.counterapp.ui.vm.states.HistoryViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCases: HistoryUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryViewState(isLoading = true))
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
            .mapResult { list -> list.map { it.toUiModel(dateFormatter) } }
            .onFailure {
                uiMessageDispatcher.dispatch(
                    Toast(message = Message.Resource(R.string.failed_to_load_history))
                )
            }
            .mapResult { list ->
                // success state
                HistoryViewState(
                    history = list,
                    isLoading = false,
                    isError = false
                )
            }
            .recoverWith {
                // single place: failure -> error state
                Result.Success(
                    HistoryViewState(
                        history = emptyList(),
                        isLoading = false,
                        isError = true
                    )
                )
            }
            .map { (it as Result.Success).data }
            .onEach { state ->
                _uiState.value = state
            }
            .launchIn(viewModelScope)
    }

    private fun clearHistory() {
        viewModelScope.launch {
            historyUseCases.clearHistoryUseCase()
                .onSuccess {
                    uiMessageDispatcher.dispatch(
                        Toast(message = Message.Resource(R.string.history_cleared))
                    )
                }
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        Toast(message = Message.Resource(R.string.failed_to_clear_history))
                    )
                }
        }
    }
}
