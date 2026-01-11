package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.CounterSnapshotParcelable
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.toUiModel
import io.droidevs.counterapp.ui.vm.actions.CounterEditAction
import io.droidevs.counterapp.ui.vm.events.CounterEditEvent
import io.droidevs.counterapp.ui.vm.states.CounterEditUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CounterEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val initialCounter: CounterUiModel = savedStateHandle.get<CounterSnapshotParcelable>("counter")
        ?.toUiModel() ?: throw IllegalArgumentException("Counter argument is required")

    private val _uiState = MutableStateFlow(CounterEditUiState(counter = initialCounter))
    val uiState: StateFlow<CounterEditUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CounterEditEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: CounterEditAction) {
        when (action) {
            is CounterEditAction.UpdateName -> {
                _uiState.update { state ->
                    state.copy(counter = state.counter?.copy(name = action.name, lastUpdatedAt = Instant.now()))
                }
            }
            is CounterEditAction.UpdateCurrentCount -> {
                _uiState.update { state ->
                    state.copy(counter = state.counter?.copy(currentCount = action.count, lastUpdatedAt = Instant.now()))
                }
            }
            is CounterEditAction.SetCanIncrease -> {
                _uiState.update { state ->
                    state.copy(counter = state.counter?.copy(canIncrease = action.canIncrease, lastUpdatedAt = Instant.now()))
                }
            }
            is CounterEditAction.SetCanDecrease -> {
                _uiState.update { state ->
                    state.copy(counter = state.counter?.copy(canDecrease = action.canDecrease, lastUpdatedAt = Instant.now()))
                }
            }
            CounterEditAction.SaveClicked -> save()
        }
    }

    private fun save() {
        val currentCounter = _uiState.value.counter ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    currentCounter.id,
                    newName = currentCounter.name,
                    newCategoryId = currentCounter.categoryId,
                    newCount = currentCounter.currentCount
                )
            )
            _uiState.update { it.copy(isSaving = false) }
            _event.emit(CounterEditEvent.CounterSaved)
        }
    }
}
