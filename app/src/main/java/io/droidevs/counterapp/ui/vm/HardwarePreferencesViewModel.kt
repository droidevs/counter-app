package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.HardwarePreferenceUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HardwarePreferencesViewModel @Inject constructor(
    private val useCases: HardwarePreferenceUseCases
) : ViewModel() {

    val hardwareButtonControl: StateFlow<Boolean> = useCases.getHardwareButtonControl()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val soundsOn: StateFlow<Boolean> = useCases.getSoundsOn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val vibrationOn: StateFlow<Boolean> = useCases.getVibrationOn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val showLabels: StateFlow<Boolean> = useCases.getLabelControl()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    // Update functions
    fun setHardwareButtonControl(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setHardwareButtonControl(enabled)
        }
    }

    fun setSoundsOn(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setSoundsOn(enabled)
        }
    }

    fun setVibrationOn(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setVibrationOn(enabled)
        }
    }

    fun setShowLabels(show: Boolean) {
        viewModelScope.launch {
            useCases.setLabelControl(show)
        }
    }
}