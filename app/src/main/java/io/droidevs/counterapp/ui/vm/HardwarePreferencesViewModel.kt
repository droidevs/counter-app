package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.usecases.preference.HardwarePreferenceUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.HardwarePreferenceAction
import io.droidevs.counterapp.ui.vm.events.HardwarePreferenceEvent
import io.droidevs.counterapp.ui.vm.states.HardwarePreferenceUiState
import io.droidevs.counterapp.ui.vm.mappers.toHardwarePreferenceUiState
import io.droidevs.counterapp.ui.vm.mappers.Quadruple
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HardwarePreferencesViewModel @Inject constructor(
    private val useCases: HardwarePreferenceUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _event = MutableSharedFlow<HardwarePreferenceEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<HardwarePreferenceEvent> = _event.asSharedFlow()

    val uiState: StateFlow<HardwarePreferenceUiState> = combine(
        useCases.getHardwareButtonControl(),
        useCases.getSoundsOn(),
        useCases.getVibrationOn(),
        useCases.getLabelControl()
    ) { hardwareButtonControl, soundsOn, vibrationOn, showLabels ->
        Quadruple(hardwareButtonControl, soundsOn, vibrationOn, showLabels).toHardwarePreferenceUiState()
    }
        .onStart { emit(HardwarePreferenceUiState()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HardwarePreferenceUiState()
        )

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
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(
                    message = Message.Resource(R.string.hardware_button_control_updated)
                )
            )
        }
    }

    private fun setSoundsOn(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setSoundsOn(enabled)
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(
                    message = Message.Resource(R.string.sounds_updated)
                )
            )
        }
    }

    private fun setVibrationOn(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setVibrationOn(enabled)
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(
                    message = Message.Resource(R.string.vibration_updated)
                )
            )
        }
    }

    private fun setShowLabels(show: Boolean) {
        viewModelScope.launch {
            useCases.setLabelControl(show)
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(
                    message = Message.Resource(R.string.labels_visibility_updated)
                )
            )
        }
    }
}
