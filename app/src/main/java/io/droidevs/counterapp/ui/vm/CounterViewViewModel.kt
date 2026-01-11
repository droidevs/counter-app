package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toParcelable
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.DeleteCounterRequest
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.CounterSnapshotParcelable
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.toUiModel
import io.droidevs.counterapp.ui.vm.actions.CounterViewAction
import io.droidevs.counterapp.ui.vm.events.CounterViewEvent
import io.droidevs.counterapp.ui.vm.states.CounterViewUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val initialCounter: CounterUiModel = savedStateHandle.get<CounterSnapshotParcelable>("counter")
        ?.toUiModel() ?: throw IllegalArgumentException("Counter argument is required")

    private val _uiState = MutableStateFlow(CounterViewUiState(counter = initialCounter))
    val uiState: StateFlow<CounterViewUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CounterViewEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: CounterViewAction) {
        when (action) {
            CounterViewAction.IncrementCounter -> increment()
            CounterViewAction.DecrementCounter -> decrement()
            CounterViewAction.ResetCounter -> reset()
            CounterViewAction.DeleteCounter -> delete()
            CounterViewAction.EditCounterClicked -> {
                _uiState.value.counter?.let {
                    viewModelScope.launch {
                        _event.emit(CounterViewEvent.NavigateToCounterEdit(it.toParcelable()))
                    }
                }
            }
        }
    }

    private fun increment() {
        val currentCounter = _uiState.value.counter ?: return
        if (!currentCounter.canIncrease) return
        
        val updated = currentCounter.copy(
            currentCount = currentCounter.currentCount + 1,
            lastUpdatedAt = java.time.Instant.now()
        )
        _uiState.update { it.copy(counter = updated) }
        save(updated)
    }

    private fun decrement() {
        val currentCounter = _uiState.value.counter ?: return
        if (!currentCounter.canDecrease) return
        
        val updated = currentCounter.copy(
            currentCount = currentCounter.currentCount - 1,
            lastUpdatedAt = java.time.Instant.now()
        )
        _uiState.update { it.copy(counter = updated) }
        save(updated)
    }

    private fun reset() {
        val currentCounter = _uiState.value.counter ?: return
        val updated = currentCounter.copy(
            currentCount = 0,
            lastUpdatedAt = java.time.Instant.now()
        )
        _uiState.update { it.copy(counter = updated) }
        save(updated)
    }

    private fun save(counter: CounterUiModel) {
        viewModelScope.launch {
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    counterId = counter.id,
                    newCount = counter.currentCount
                )
            )
        }
    }

    private fun delete() {
        val currentCounter = _uiState.value.counter ?: return
        viewModelScope.launch {
            counterUseCases.deleteCounter(DeleteCounterRequest.of(counterId = currentCounter.id))
            _event.emit(CounterViewEvent.NavigateBack)
        }
    }
}
