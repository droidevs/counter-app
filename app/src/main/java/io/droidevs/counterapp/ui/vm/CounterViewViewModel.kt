package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
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
import io.droidevs.counterapp.ui.vm.states.CounterViewUiState
import io.droidevs.counterapp.ui.vm.mappers.toViewUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private val counterId: String = savedStateHandle.get<String>("counterId")
        ?: throw IllegalArgumentException("Counter ID is required")

    private val _event = MutableSharedFlow<CounterViewEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    val uiState: StateFlow<CounterViewUiState> = counterUseCases.getCounter(counterId)
        .map { counter ->
            counter?.toUiModel(dateFormatter)?.toViewUiState(isLoading = false)
                ?: CounterViewUiState(isLoading = false) // Or handle error state
        }
        .onStart { emit(CounterViewUiState(isLoading = true)) } // Initial loading state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CounterViewUiState()
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
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counterId,
                    newCount = currentCounter.currentCount + 1
                )
            )
        }
    }

    private fun decrement() {
        val currentCounter = uiState.value.counter ?: return
        if (!currentCounter.canDecrease) return

        viewModelScope.launch {
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counterId,
                    newCount = currentCounter.currentCount - 1
                )
            )
        }
    }

    private fun reset() {
        viewModelScope.launch {
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counterId,
                    newCount = 0
                )
            )
        }
    }

    private fun delete() {
        viewModelScope.launch {
            val counterName = uiState.value.counter?.name ?: "Counter"
            counterUseCases.deleteCounter(DeleteCounterRequest.of(counterId = counterId))
            uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.counter_deleted, args = arrayOf(counterName))))
            _event.emit(CounterViewEvent.NavigateBack)
        }
    }
}
