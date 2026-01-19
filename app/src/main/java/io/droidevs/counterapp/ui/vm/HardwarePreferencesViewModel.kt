package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.result.onFailureSuspend
import io.droidevs.counterapp.domain.result.onSuccessSuspend
import io.droidevs.counterapp.domain.result.recoverWith
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
    val event = _event.asSharedFlow()

    val uiState: StateFlow<HardwarePreferenceUiState> = combine(
        useCases.getKeepScreenOn(),
        useCases.getHardwareButtonControl(),
        useCases.getSoundsOn(),
        useCases.getVibrationOn(),
    ) { keepScreenOnResult, hardwareButtonControlResult, soundsOnResult, vibrationOnResult ->

        when {
            keepScreenOnResult is Result.Failure -> Result.Failure(keepScreenOnResult.error)
            hardwareButtonControlResult is Result.Failure -> Result.Failure(hardwareButtonControlResult.error)
            soundsOnResult is Result.Failure -> Result.Failure(soundsOnResult.error)
            vibrationOnResult is Result.Failure -> Result.Failure(vibrationOnResult.error)
            else -> Result.Success(
                HardwarePreferenceUiState(
                    keepScreenOn = keepScreenOnResult.dataOr { false },
                    hardwareButtonControl = hardwareButtonControlResult.dataOr { false },
                    soundsOn = soundsOnResult.dataOr { false },
                    vibrationOn = vibrationOnResult.dataOr { false },
                    error = false,
                    isLoading = false
                )
            )
        }
    }
        .recoverWith {
            Result.Success(
                HardwarePreferenceUiState(
                    keepScreenOn = false,
                    hardwareButtonControl = false,
                    soundsOn = false,
                    vibrationOn = false,
                    error = true,
                    isLoading = false
                )
            )
        }
        .map { (it as Result.Success).data }
        .onStart { emit(HardwarePreferenceUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HardwarePreferenceUiState(isLoading = true)
        )

    fun onAction(action: HardwarePreferenceAction) {
        when (action) {
            is HardwarePreferenceAction.SetKeepScreenOn -> setKeepScreenOn(action.keep)
            is HardwarePreferenceAction.SetHardwareButtonControl -> setHardwareButtonControl(action.enabled)
            is HardwarePreferenceAction.SetSoundsOn -> setSoundsOn(action.enabled)
            is HardwarePreferenceAction.SetVibrationOn -> setVibrationOn(action.enabled)
        }
    }

    private fun setKeepScreenOn(keep: Boolean) {
        viewModelScope.launch {
            useCases.setKeepScreenOn(keep)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.keep_screen_on_updated))
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(message = Message.Resource(R.string.failed_to_update_keep_screen_on))
                    )
                }
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
}
