package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.usecases.counters.RemoveAllCountersUseCase
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.onFailureSuspend
import io.droidevs.counterapp.domain.result.onSuccessSuspend
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage.Toast
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.OtherPreferencesAction
import io.droidevs.counterapp.ui.vm.events.OtherPreferencesEvent
import io.droidevs.counterapp.ui.vm.states.OtherPreferencesUiState
import io.droidevs.counterapp.util.TracingHelper
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
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val tracing: TracingHelper
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
        // run inside a coroutine and create a Sentry span around the suspend call
        viewModelScope.launch {
            tracing.tracedSuspend("otherprefs_show_remove_confirmation") {
                _event.send(OtherPreferencesEvent.ShowRemoveConfirmation)
            }
        }
    }

    private fun removeAllCounters() {
        // keep existing coroutine scope but use tracedSuspendResult for the usecase call
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val result = tracing.tracedSuspendResult("otherprefs_remove_all_usecase") {
                removeAllCountersUseCase()
            }.onSuccessSuspend {
                _state.value = _state.value.copy(isLoading = false)
                uiMessageDispatcher.dispatch(
                    Toast(
                        message = Message.Resource(
                            R.string.counters_removed_successfully
                        )
                    )
                )
            }.onFailureSuspend {
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
