package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.CounterPreferenceUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterPreferencesViewModel @Inject constructor(
    private val useCases: CounterPreferenceUseCases
) : ViewModel() {

    // Flows exposed to UI - using stateIn for caching + proper lifecycle handling
    val incrementStep: StateFlow<Int> = useCases.getCounterIncrementStep()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 1
        )

    val defaultValue: StateFlow<Int> = useCases.getDefaultCounterValue()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val minimumValue: StateFlow<Int?> = useCases.getMinimumCounterValue()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val maximumValue: StateFlow<Int?> = useCases.getMaximumCounterValue()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Update methods
    fun setIncrementStep(value: Int) {
        viewModelScope.launch {
            useCases.setCounterIncrementStep(value.coerceAtLeast(1))
        }
    }

    fun setDefaultValue(value: Int) {
        viewModelScope.launch {
            useCases.setDefaultCounterValue(value)
        }
    }

    fun setMinimumValue(value: Int?) {
        viewModelScope.launch {
            useCases.setMinimumCounterValue(value)
        }
    }

    fun setMaximumValue(value: Int?) {
        viewModelScope.launch {
            useCases.setMaximumCounterValue(value)
        }
    }
}