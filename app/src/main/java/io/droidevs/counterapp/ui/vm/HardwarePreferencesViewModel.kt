package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.getOrNull
import io.droidevs.counterapp.domain.result.onFailureSuspend
import io.droidevs.counterapp.domain.result.onSuccessSuspend
import io.droidevs.counterapp.domain.usecases.preference.HardwarePreferenceUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.HardwarePreferenceAction
import io.droidevs.counterapp.ui.vm.events.HardwarePreferenceEvent
import io.droidevs.counterapp.ui.vm.states.HardwarePreferenceUiState
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
    ) { hardwareButtonControlResult, soundsOnResult, vibrationOnResult, showLabelsResult ->
        val hardwareButtonControl = hardwareButtonControlResult.getOrNull() ?: false
        val soundsOn = soundsOnResult.getOrNull() ?: false
        val vibrationOn = vibrationOnResult.getOrNull() ?: false
        val showLabels = showLabelsResult.getOrNull() ?: false

        val hasError = listOf(
            hardwareButtonControlResult,
            soundsOnResult,
            vibrationOnResult,
            showLabelsResult
        ).any { it is Result.Failure }

        HardwarePreferenceUiState(
            hardwareButtonControl = hardwareButtonControl,
            soundsOn = soundsOn,
            vibrationOn = vibrationOn,
            showLabels = showLabels,
            error = hasError,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HardwarePreferenceUiState(isLoading = true)
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
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.hardware_button_control_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_hardware_button_control))
                    )
                }
        }
    }

    private fun setSoundsOn(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setSoundsOn(enabled)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.sounds_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_sounds))
                    )
                }
        }
    }

    private fun setVibrationOn(enabled: Boolean) {
        viewModelScope.launch {
            useCases.setVibrationOn(enabled)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.vibration_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_vibration))
                    )
                }
        }
    }

    private fun setShowLabels(show: Boolean) {
        viewModelScope.launch {
            useCases.setLabelControl(show)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.labels_visibility_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_labels_visibility))
                    )
                }
        }
    }
}
