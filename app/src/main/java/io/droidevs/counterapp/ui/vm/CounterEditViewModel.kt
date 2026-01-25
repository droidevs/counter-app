package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.SavedStateHandle
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
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.requests.UpdateCounterRequest
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.CounterEditAction
import io.droidevs.counterapp.ui.vm.events.CounterEditEvent
import io.droidevs.counterapp.ui.vm.mappers.toEditUiState
import io.droidevs.counterapp.ui.vm.states.CounterEditUiState
import io.droidevs.counterapp.util.TracingHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CounterEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher,
    private val dateFormatter: DateFormatter,
    private val tracing: TracingHelper
) : ViewModel() {

    private data class OverridesCache(
        val incrementStep: Int? = null,
        val decrementStep: Int? = null,
        val defaultValue: Int? = null,
        val minValue: Int? = null,
        val maxValue: Int? = null,
    )

    private var overridesCache: OverridesCache = OverridesCache()

    private val counterId: String = savedStateHandle.get<String>("counterId")!!

    private val _event = MutableSharedFlow<CounterEditEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    private val _editableCounter = MutableStateFlow<io.droidevs.counterapp.ui.models.CounterUiModel?>(null)
    private val _isSaving = MutableStateFlow(false)

    private val _loadingState: StateFlow<CounterEditUiState> = counterUseCases.getCounter(counterId)
        .mapResult { domainCounter -> domainCounter.toUiModel(dateFormatter) }
        .onFailure { error ->
            when (error) {
                is io.droidevs.counterapp.domain.result.errors.DatabaseError.NotFound ->
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.counter_not_found)))
                is io.droidevs.counterapp.domain.result.errors.DatabaseError.QueryFailed ->
                    uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_counter)))
                else -> uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.failed_to_load_counter)))
            }
        }
        .mapResult { uiModel ->
            _editableCounter.value = uiModel
            uiModel.toEditUiState(isLoading = false, isSaving = false, isError = false)
        }
        .recoverWith {
            Result.Success(CounterEditUiState(isLoading = false, isError = true, isSaving = false))
        }
        .map { (it as Result.Success).data }
        .onStart { emit(CounterEditUiState(isLoading = true, isError = false, isSaving = false)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CounterEditUiState(isLoading = true, isError = false, isSaving = false)
        )

    val uiState: StateFlow<CounterEditUiState> = combine(
        _loadingState,
        _editableCounter,
        _isSaving
    ) { loadState, counter, isSaving ->
        when {
            loadState.isError -> loadState.copy(isSaving = isSaving)
            counter == null -> loadState.copy(isSaving = isSaving)
            else -> counter.toEditUiState(isLoading = false, isSaving = isSaving, isError = false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CounterEditUiState(isLoading = true, isError = false)
    )

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

            is CounterEditAction.UseDefaultBehaviorChanged -> {
                val checked = action.value

                _editableCounter.update { current ->
                    current ?: return@update null

                    if (checked) {
                        overridesCache = OverridesCache(
                            incrementStep = current.incrementStep,
                            decrementStep = current.decrementStep,
                            defaultValue = current.defaultValue,
                            minValue = current.minValue,
                            maxValue = current.maxValue,
                        )
                        current.copy(
                            useDefaultBehavior = true,
                            incrementStep = null,
                            decrementStep = null,
                            defaultValue = null,
                            minValue = null,
                            maxValue = null,
                        )
                    } else {
                        current.copy(
                            useDefaultBehavior = false,
                            incrementStep = overridesCache.incrementStep,
                            decrementStep = overridesCache.decrementStep,
                            defaultValue = overridesCache.defaultValue,
                            minValue = overridesCache.minValue,
                            maxValue = overridesCache.maxValue,
                        )
                    }
                }
            }

            is CounterEditAction.IncrementStepChanged -> {
                val parsed = action.value.trim().toIntOrNull()?.coerceAtLeast(1)
                _editableCounter.update { it?.copy(incrementStep = parsed) }
            }

            is CounterEditAction.DecrementStepChanged -> {
                val parsed = action.value.trim().toIntOrNull()?.coerceAtLeast(1)
                _editableCounter.update { it?.copy(decrementStep = parsed) }
            }

            is CounterEditAction.DefaultValueChanged -> {
                val parsed = action.value.trim().toIntOrNull()
                _editableCounter.update { it?.copy(defaultValue = parsed) }
            }

            is CounterEditAction.MinValueChanged -> {
                val parsed = action.value.trim().toIntOrNull()
                _editableCounter.update { it?.copy(minValue = parsed) }
            }

            is CounterEditAction.MaxValueChanged -> {
                val parsed = action.value.trim().toIntOrNull()
                _editableCounter.update { it?.copy(maxValue = parsed) }
            }

            CounterEditAction.SaveClicked -> {
                saveCounter()
            }
        }
    }

    private fun saveCounter() {
        val counter = _editableCounter.value

        if (counter != null) {
            // validate legacy rule
            if (!counter.canIncrease && counter.canDecrease && counter.currentCount <= 0) {
                uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.error_decrement_only_counter)))
                return
            }

            // Validate bounds relationship
            val min = counter.minValue
            val max = counter.maxValue
            if (min != null && max != null && min > max) {
                uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.error_min_gt_max)))
                return
            }

            // Validate current count against bounds if bounds exist
            if (min != null && counter.currentCount < min) {
                uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.error_count_outside_bounds)))
                return
            }
            if (max != null && counter.currentCount > max) {
                uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(resId = R.string.error_count_outside_bounds)))
                return
            }

            viewModelScope.launch {
                tracing.tracedSuspend("counteredit_save_counter") {
                    _isSaving.value = true
                    val request = UpdateCounterRequest(
                        counterId = counter.id,
                        newName = counter.name,
                        newCount = counter.currentCount,
                        canIncrease = counter.canIncrease,
                        canDecrease = counter.canDecrease,

                        // If using defaults, explicitly clear any per-counter overrides.
                        incrementStep = if (counter.useDefaultBehavior) null else counter.incrementStep,
                        decrementStep = if (counter.useDefaultBehavior) null else counter.decrementStep,
                        minValue = if (counter.useDefaultBehavior) null else counter.minValue,
                        maxValue = if (counter.useDefaultBehavior) null else counter.maxValue,
                        defaultValue = if (counter.useDefaultBehavior) null else counter.defaultValue,
                        useDefaultBehavior = counter.useDefaultBehavior,

                        lastUpdatedAt = Instant.now(),
                        orderAnchorAt = Instant.now()
                    )
                    counterUseCases.updateCounter(request)
                }
                    .onSuccess {
                        uiMessageDispatcher.dispatch(UiMessage.Toast(message = Message.Resource(R.string.counter_saved)))
                        _event.tryEmit(CounterEditEvent.NavigateBack)
                    }
                    .onFailure {
                        uiMessageDispatcher.dispatch(
                            UiMessage.Toast(message = Message.Resource(R.string.failed_to_save_counter))
                        )
                    }
                _isSaving.value = false
            }
        }
    }
}
