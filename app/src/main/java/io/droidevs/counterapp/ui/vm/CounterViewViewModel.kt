package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.mapResult
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.onSuccessSuspend
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.DeleteCounterRequest
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.CounterViewAction
import io.droidevs.counterapp.ui.vm.events.CounterViewEvent
import io.droidevs.counterapp.ui.vm.mappers.toViewUiState
import io.droidevs.counterapp.ui.vm.states.CounterViewUiState
import io.droidevs.counterapp.domain.feedback.CounterFeedbackAction
import io.droidevs.counterapp.domain.feedback.CounterFeedbackManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val dateFormatter: DateFormatter,
    private val feedbackManager: CounterFeedbackManager,
) : ViewModel() {

    private val counterId: String = savedStateHandle.get<String>("counterId")
        ?: throw IllegalArgumentException("Counter ID is required")

    private val _event = MutableSharedFlow<CounterViewEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    val uiState: StateFlow<CounterViewUiState> = counterUseCases.getCounter(counterId)
        .mapResult { counter ->
            counter.toUiModel(dateFormatter).toViewUiState(isLoading = false)
        }
        .onFailure { error ->
            // Dispatch a specific message based on error type
            when (error) {
                is io.droidevs.counterapp.domain.result.errors.DatabaseError.NotFound ->
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.counter_not_found)))
                is io.droidevs.counterapp.domain.result.errors.DatabaseError.QueryFailed ->
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_counter)))
                else -> uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_counter)))
            }
        }
        // Convert failure into a SUCCESS ui state with error flags (so UI never needs getOrNull).
        .recoverWith {
            Result.Success(
                CounterViewUiState(
                    counter = null,
                    isLoading = false,
                    isError = true
                )
            )
        }
        // Now every emission is a Success<CounterViewUiState>
        .map { (it as Result.Success).data }
        .onStart { emit(CounterViewUiState(isLoading = true, isError = false)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CounterViewUiState(isLoading = true, isError = false)
        )

    fun onAction(action: CounterViewAction) {
        when (action) {
            CounterViewAction.IncrementCounter -> increment()
            CounterViewAction.DecrementCounter -> decrement()
            CounterViewAction.ResetCounter -> reset()
            CounterViewAction.DeleteCounter -> delete()
            CounterViewAction.EditCounterClicked -> {
                viewModelScope.launch {
                    _event.emit(CounterViewEvent.NavigateToCounterEdit(counterId))
                }
            }
        }
    }

    private fun increment() {
        val currentCounter = uiState.value.counter ?: return
        if (!currentCounter.canIncrease) return

        viewModelScope.launch {
            counterUseCases.incrementCounter(currentCounter.toDomain())
                .onSuccessSuspend {
                    feedbackManager.onAction(CounterFeedbackAction.INCREMENT)
                }
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_increment_counter))
                    )
                }
        }
    }

    private fun decrement() {
        val currentCounter = uiState.value.counter ?: return
        if (!currentCounter.canDecrease) return

        viewModelScope.launch {
            counterUseCases.decrementCounter(currentCounter.toDomain())
                .onSuccessSuspend {
                    feedbackManager.onAction(CounterFeedbackAction.DECREMENT)
                }
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_decrement_counter))
                    )
                }
        }
    }

    private fun reset() {
        viewModelScope.launch {
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counterId,
                    newCount = 0
                )
            ).onSuccessSuspend {
                feedbackManager.onAction(CounterFeedbackAction.RESET)
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(R.string.counter_reset_success))
                )
            }.onFailure {
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(message = Message.Resource(R.string.failed_to_reset_counter))
                )
            }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            val counterName = uiState.value.counter?.name ?: ""
            counterUseCases.deleteCounter(DeleteCounterRequest.of(counterId = counterId))
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(
                                resId = R.string.counter_deleted,
                                args = arrayOf(counterName)
                            )
                        )
                    )
                    _event.emit(CounterViewEvent.NavigateBack)
                }
                .onFailure {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_delete_counter))
                    )
                }
        }
    }
}