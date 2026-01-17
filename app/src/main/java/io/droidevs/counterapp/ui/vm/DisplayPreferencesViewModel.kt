package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.result.onFailureSuspend
import io.droidevs.counterapp.domain.result.onSuccessSuspend
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.usecases.preference.DisplayPreferenceUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.DisplayPreferenceAction
import io.droidevs.counterapp.ui.vm.events.DisplayPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.DisplayPreferenceUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
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
class DisplayPreferencesViewModel @Inject constructor(
    private val useCases: DisplayPreferenceUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModel() {

    private val _event = MutableSharedFlow<DisplayPreferenceEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    val uiState: StateFlow<DisplayPreferenceUiState> = combine(
        useCases.getTheme(),
        useCases.getHideControls(),
        useCases.getHideLastUpdate(),
        useCases.getKeepScreenOn(),
    ) { themeResult, hideControlsResult, hideLastUpdateResult, keepScreenOnResult ->

        when {
            themeResult is Result.Failure -> Result.Failure(themeResult.error)
            hideControlsResult is Result.Failure -> Result.Failure(hideControlsResult.error)
            hideLastUpdateResult is Result.Failure -> Result.Failure(hideLastUpdateResult.error)
            keepScreenOnResult is Result.Failure -> Result.Failure(keepScreenOnResult.error)
            else -> Result.Success(
                DisplayPreferenceUiState(
                    theme = themeResult.dataOr { Theme.SYSTEM },
                    hideControls = hideControlsResult.dataOr { false },
                    hideLastUpdate = hideLastUpdateResult.dataOr { false },
                    keepScreenOn = keepScreenOnResult.dataOr { false },
                    error = false,
                    isLoading = false
                )
            )
        }
    }
        .recoverWith {
            Result.Success(
                DisplayPreferenceUiState(
                    theme = Theme.SYSTEM,
                    hideControls = false,
                    hideLastUpdate = false,
                    keepScreenOn = false,
                    error = true,
                    isLoading = false
                )
            )
        }
        .map { (it as Result.Success).data }
        .onStart { emit(DisplayPreferenceUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DisplayPreferenceUiState(isLoading = true)
        )

    fun onAction(action: DisplayPreferenceAction) {
        when (action) {
            is DisplayPreferenceAction.SetTheme -> setTheme(action.theme)
            is DisplayPreferenceAction.SetHideControls -> setHideControls(action.hide)
            is DisplayPreferenceAction.SetHideLastUpdate -> setHideLastUpdate(action.hide)
            is DisplayPreferenceAction.SetKeepScreenOn -> setKeepScreenOn(action.keep)
        }
    }

    private fun setTheme(value: Theme) {
        viewModelScope.launch {
            useCases.setTheme(value)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.theme_updated)
                        )
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.failed_to_update_theme)
                        )
                    )
                }
        }
    }

    private fun setHideControls(hide: Boolean) {
        viewModelScope.launch {
            useCases.setHideControls(hide)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.controls_visibility_updated)
                        )
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.failed_to_update_hide_controls)
                        )
                    )
                }
        }
    }

    private fun setHideLastUpdate(hide: Boolean) {
        viewModelScope.launch {
            useCases.setHideLastUpdate(hide)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.last_update_visibility_updated)
                        )
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.failed_to_update_hide_last_update)
                        )
                    )
                }
        }
    }

    private fun setKeepScreenOn(keep: Boolean) {
        viewModelScope.launch {
            useCases.setKeepScreenOn(keep)
                .onSuccessSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.keep_screen_on_updated)
                        )
                    )
                }
                .onFailureSuspend {
                    uiMessageDispatcher.dispatch(
                        UiMessage.Toast(
                            message = Message.Resource(R.string.failed_to_update_keep_screen_on)
                        )
                    )
                }
        }
    }
}
