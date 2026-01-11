package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toParcelable
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.DeleteCounterRequest
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.CounterSnapshotParcelable
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.toUiModel
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
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val _event = MutableSharedFlow<CounterViewEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    val uiState: StateFlow<CounterViewUiState> = flow {
        val initialCounterId: String = savedStateHandle.get<CounterSnapshotParcelable>("counter")
            ?.id ?: throw IllegalArgumentException("Counter argument is required")
        // Fetch the counter from the database using its ID
        counterUseCases.getCounter(initialCounterId)
            .collect { domainCounter ->
                domainCounter?.let { emit(it.toUiModel()) }
            }
    }
        .map { it.toViewUiState(isLoading = false) }
        .onStart { emit(CounterViewUiState(isLoading = true)) } // Initial loading state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CounterViewUiState() // Default empty state
        )

    fun onAction(action: CounterViewAction) {
        when (action) {
            CounterViewAction.IncrementCounter -> increment()
            CounterViewAction.DecrementCounter -> decrement()
            CounterViewAction.ResetCounter -> reset()
            CounterViewAction.DeleteCounter -> delete()
            CounterViewAction.EditCounterClicked -> {
                uiState.value.counter?.let {
                    viewModelScope.launch {
                        _event.emit(CounterViewEvent.NavigateToCounterEdit(it.toParcelable()))
                    }
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
                    counterId = currentCounter.id,
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
                    counterId = currentCounter.id,
                    newCount = currentCounter.currentCount - 1
                )
            )
        }
    }

    private fun reset() {
        val currentCounter = uiState.value.counter ?: return
        viewModelScope.launch {
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = currentCounter.id,
                    newCount = 0
                )
            )
        }
    }

    private fun delete() {
        val currentCounter = uiState.value.counter ?: return
        viewModelScope.launch {
            counterUseCases.deleteCounter(DeleteCounterRequest.of(counterId = currentCounter.id))
            _event.emit(CounterViewEvent.NavigateBack)
        }
    }
}
