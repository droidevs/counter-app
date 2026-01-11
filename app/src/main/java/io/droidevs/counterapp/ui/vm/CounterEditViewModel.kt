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
import io.droidevs.counterapp.ui.vm.mappers.toEditUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CounterEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val _event = MutableSharedFlow<CounterEditEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    private val _editableCounter = MutableStateFlow<CounterUiModel?>(null)
    private val _isSaving = MutableStateFlow(false)

    val uiState: StateFlow<CounterEditUiState> = combine(
        _editableCounter,
        _isSaving
    ) { counter, isSaving ->
        counter?.toEditUiState(isLoading = false, isSaving = isSaving) ?: CounterEditUiState(isLoading = true)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CounterEditUiState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            val initialCounter: CounterUiModel = savedStateHandle.get<CounterSnapshotParcelable>("counter")
                ?.toUiModel() ?: throw IllegalArgumentException("Counter argument is required")
            _editableCounter.value = initialCounter
        }
    }

    fun onAction(action: CounterEditAction) {
        when (action) {
            is CounterEditAction.UpdateName -> {
                _editableCounter.update { currentCounter ->
                    currentCounter?.copy(name = action.name, lastUpdatedAt = Instant.now())
                }
            }
            is CounterEditAction.UpdateCurrentCount -> {
                _editableCounter.update { currentCounter ->
                    currentCounter?.copy(currentCount = action.count, lastUpdatedAt = Instant.now())
                }
            }
            is CounterEditAction.SetCanIncrease -> {
                _editableCounter.update { currentCounter ->
                    currentCounter?.copy(canIncrease = action.canIncrease, lastUpdatedAt = Instant.now())
                }
            }
            is CounterEditAction.SetCanDecrease -> {
                _editableCounter.update { currentCounter ->
                    currentCounter?.copy(canDecrease = action.canDecrease, lastUpdatedAt = Instant.now())
                }
            }
            CounterEditAction.SaveClicked -> save()
        }
    }

    private fun save() {
        val currentCounter = _editableCounter.value ?: return
        viewModelScope.launch {
            _isSaving.value = true
            counterUseCases.updateCounter(
                UpdateCounterRequest.of(
                    currentCounter.id,
                    newName = currentCounter.name,
                    newCategoryId = currentCounter.categoryId,
                    newCount = currentCounter.currentCount,
                    canIncrease = currentCounter.canIncrease,
                    canDecrease = currentCounter.canDecrease
                )
            )
            _isSaving.value = false
            _event.emit(CounterEditEvent.CounterSaved)
        }
    }
}
