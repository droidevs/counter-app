package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.CounterEditAction
import io.droidevs.counterapp.ui.vm.events.CounterEditEvent
import io.droidevs.counterapp.ui.vm.mappers.toEditUiState
import io.droidevs.counterapp.ui.vm.states.CounterEditUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CounterEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private val counterId: String = savedStateHandle.get<String>("counterId")!!

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
            counterUseCases.getCounter(counterId).collect { domainCounter ->
                _editableCounter.value = domainCounter?.toUiModel(dateFormatter)
            }
        }
    }

    fun onAction(action: CounterEditAction) {
        when (action) {
            is CounterEditAction.UpdateName -> {
                _editableCounter.update { it?.copy(name = action.name) }
            }
            is CounterEditAction.UpdateCurrentCount -> {
                _editableCounter.update { it?.copy(currentCount = action.count) }
            }
            is CounterEditAction.SetCanIncrease -> {
                _editableCounter.update { counter ->
                    counter?.copy(
                        canIncrease = action.canIncrease,
                        canDecrease = if (!action.canIncrease) true else counter.canDecrease
                    )
                }
            }
            is CounterEditAction.SetCanDecrease -> {
                _editableCounter.update { counter ->
                    counter?.copy(
                        canDecrease = action.canDecrease,
                        canIncrease = if (!action.canDecrease) true else counter.canIncrease
                    )
                }
            }
            CounterEditAction.SaveClicked -> {
                saveCounter()
            }
        }
    }

    private fun saveCounter() {
        val counter = _editableCounter.value

        if (counter != null) {
            if (!counter.canIncrease && counter.canDecrease && counter.currentCount <= 0) {
                uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.error_decrement_only_counter)))
                return
            }

            viewModelScope.launch {
                _isSaving.value = true
                val request = UpdateCounterRequest(
                    counterId = counter.id,
                    newName = counter.name,
                    newCount = counter.currentCount,
                    canIncrease = counter.canIncrease,
                    canDecrease = counter.canDecrease,
                    lastUpdatedAt = Instant.now(),
                    orderAnchorAt = Instant.now()
                )
                counterUseCases.updateCounter(request)
                _isSaving.value = false
                uiMessageDispatcher.dispatch(
                    UiMessage.Toast(
                        message = Message.Resource(R.string.counter_saved)
                    )
                )
                _event.emit(CounterEditEvent.NavigateBack)
            }
        }
    }
}
