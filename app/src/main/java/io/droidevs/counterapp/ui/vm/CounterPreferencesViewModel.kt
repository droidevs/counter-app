package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.CounterPreferenceUseCases
import io.droidevs.counterapp.ui.vm.actions.CounterBehaviorPreferenceAction
import io.droidevs.counterapp.ui.vm.events.CounterBehaviorPreferenceEvent
import io.droidevs.counterapp.ui.vm.mappers.Quintuple
import io.droidevs.counterapp.ui.vm.states.CounterBehaviorPreferenceUiState
import io.droidevs.counterapp.ui.vm.mappers.toCounterBehaviorPreferenceUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterPreferencesViewModel @Inject constructor(
    private val useCases: CounterPreferenceUseCases
) : ViewModel() {

    private val _event = MutableSharedFlow<CounterBehaviorPreferenceEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CounterBehaviorPreferenceEvent> = _event.asSharedFlow()

    val uiState: StateFlow<CounterBehaviorPreferenceUiState> = combine(
        useCases.getCounterIncrementStep(),
        useCases.getCounterDecrementStep(),
        useCases.getDefaultCounterValue(),
        useCases.getMinimumCounterValue().onStart { emit(null) }, // Provide a default null for initial combine
        useCases.getMaximumCounterValue().onStart { emit(null) }
    ) { incrementStep, decrementStep, defaultValue, minimumValue, maximumValue ->
        Quintuple(incrementStep, decrementStep, defaultValue, minimumValue, maximumValue).toCounterBehaviorPreferenceUiState()
    }
        .onStart { emit(CounterBehaviorPreferenceUiState()) } // Initial default state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CounterBehaviorPreferenceUiState()
        )

    fun onAction(action: CounterBehaviorPreferenceAction) {
        when (action) {
            is CounterBehaviorPreferenceAction.SetCounterIncrementStep -> setIncrementStep(action.step)
            is CounterBehaviorPreferenceAction.SetCounterDecrementStep -> setDecrementStep(action.step)
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

    private fun setDecrementStep(value: Int) {
        viewModelScope.launch {
            useCases.setCounterDecrementStep(value.coerceAtLeast(1))
            _event.emit(CounterBehaviorPreferenceEvent.ShowMessage("Decrement step updated"))
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
