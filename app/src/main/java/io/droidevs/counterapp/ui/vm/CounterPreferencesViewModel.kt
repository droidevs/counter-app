package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.CounterPreferenceUseCases
import io.droidevs.counterapp.ui.vm.actions.CounterBehaviorPreferenceAction
import io.droidevs.counterapp.ui.vm.events.CounterBehaviorPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.CounterBehaviorPreferenceUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterPreferencesViewModel @Inject constructor(
    private val useCases: CounterPreferenceUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CounterBehaviorPreferenceUiState())
    val uiState: StateFlow<CounterBehaviorPreferenceUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CounterBehaviorPreferenceEvent>()
    val event: SharedFlow<CounterBehaviorPreferenceEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            useCases.getCounterIncrementStep().collectLatest { step ->
                _uiState.update { it.copy(counterIncrementStep = step) }
            }
        }
        viewModelScope.launch {
            useCases.getDefaultCounterValue().collectLatest { value ->
                _uiState.update { it.copy(defaultCounterValue = value) }
            }
        }
        viewModelScope.launch {
            useCases.getMinimumCounterValue().collectLatest { value ->
                _uiState.update { it.copy(minimumCounterValue = value ?: 0) }
            }
        }
        viewModelScope.launch {
            useCases.getMaximumCounterValue().collectLatest { value ->
                _uiState.update { it.copy(maximumCounterValue = value ?: 1000) }
            }
        }
    }

    fun onAction(action: CounterBehaviorPreferenceAction) {
        when (action) {
            is CounterBehaviorPreferenceAction.SetCounterIncrementStep -> setIncrementStep(action.step)
            is CounterBehaviorPreferenceAction.SetDefaultCounterValue -> setDefaultValue(action.value)
            is CounterBehaviorPreferenceAction.SetMaximumCounterValue -> setMaximumValue(action.value)
            is CounterBehaviorPreferenceAction.SetMinimumCounterValue -> setMinimumValue(action.value)
        }
    }

    private fun setIncrementStep(value: Int) {
        viewModelScope.launch {
            useCases.setCounterIncrementStep(value.coerceAtLeast(1))
            _event.emit(CounterBehaviorPreferenceEvent.ShowMessage("Increment step updated"))
        }
    }

    private fun setDefaultValue(value: Int) {
        viewModelScope.launch {
            useCases.setDefaultCounterValue(value)
            _event.emit(CounterBehaviorPreferenceEvent.ShowMessage("Default value updated"))
        }
    }

    private fun setMinimumValue(value: Int?) {
        viewModelScope.launch {
            useCases.setMinimumCounterValue(value)
            _event.emit(CounterBehaviorPreferenceEvent.ShowMessage("Minimum value updated"))
        }
    }

    private fun setMaximumValue(value: Int?) {
        viewModelScope.launch {
            useCases.setMaximumCounterValue(value)
            _event.emit(CounterBehaviorPreferenceEvent.ShowMessage("Maximum value updated"))
        }
    }
}
