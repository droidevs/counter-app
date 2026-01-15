package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.usecases.counters.RemoveAllCountersUseCase
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.UiMessage.Toast
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
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
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _event = Channel<OtherPreferencesEvent>(Channel.BUFFERED)
    val event: Flow<OtherPreferencesEvent> = _event.receiveAsFlow()

    private val _state = MutableStateFlow(OtherPreferencesUiState())
    val state: StateFlow<OtherPreferencesUiState> = _state.asStateFlow()

    fun onAction(action: OtherPreferencesAction) {
        when (action) {
            OtherPreferencesAction.RemoveCounters -> showRemoveConfirmation()
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
                uiMessageDispatcher.dispatch(
                    Toast(
                        message = Message.Resource(
                            R.string.counters_removed_successfully
                        )
                    )
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
                uiMessageDispatcher.dispatch(
                    Toast(
                        message = Message.Resource(
                            R.string.failed_to_remove_counters
                        )
                    )
                )
            }
        }
    }
}
