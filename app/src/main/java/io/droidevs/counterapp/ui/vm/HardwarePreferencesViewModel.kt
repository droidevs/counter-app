package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.preference.HardwarePreferenceUseCases
import io.droidevs.counterapp.ui.vm.actions.HardwarePreferenceAction
import io.droidevs.counterapp.ui.vm.events.HardwarePreferenceEvent
import io.droidevs.counterapp.ui.vm.states.HardwarePreferenceUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HardwarePreferencesViewModel @Inject constructor(
    private val useCases: HardwarePreferenceUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(HardwarePreferenceUiState())
    val uiState: StateFlow<HardwarePreferenceUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HardwarePreferenceEvent>()
    val event: SharedFlow<HardwarePreferenceEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            useCases.getHardwareButtonControl().collectLatest { enabled ->
                _uiState.update { it.copy(hardwareButtonControl = enabled) }
            }
        }
        viewModelScope.launch {
            useCases.getSoundsOn().collectLatest { enabled ->
                _uiState.update { it.copy(soundsOn = enabled) }
            }
        }
        viewModelScope.launch {
            useCases.getVibrationOn().collectLatest { enabled ->
                _uiState.update { it.copy(vibrationOn = enabled) }
            }
        }
        viewModelScope.launch {
            useCases.getLabelControl().collectLatest { show ->
                _uiState.update { it.copy(showLabels = show) }
            }
        }
    }

    fun onAction(action: HardwarePreferenceAction) {
        when (action) {
            is HardwarePreferenceAction.SetHardwareButtonControl -> setHardwareButtonControl(action.enabled)
            is HardwarePreferenceAction.SetSoundsOn -> setSoundsOn(action.enabled)
            is HardwarePreferenceAction.SetVibrationOn -> setVibrationOn(action.enabled)
            is HardwarePreferenceAction.SetShowLabels -> setShowLabels(action.show)
        }
    }

    private fun setHardwareButtonControl(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setHardwareButtonControl(enabled)
            _event.emit(HardwarePreferenceEvent.ShowMessage("Hardware button control updated"))
        }
    }

    private fun setSoundsOn(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setSoundsOn(enabled)
            _event.emit(HardwarePreferenceEvent.ShowMessage("Sounds updated"))
        }
    }

    private fun setVibrationOn(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setVibrationOn(enabled)
            _event.emit(HardwarePreferenceEvent.ShowMessage("Vibration updated"))
        }
    }

    private fun setShowLabels(show: Boolean) {
        viewModelScope.launch {
            useCases.setLabelControl(show)
            _event.emit(HardwarePreferenceEvent.ShowMessage("Labels visibility updated"))
        }
    }
}
