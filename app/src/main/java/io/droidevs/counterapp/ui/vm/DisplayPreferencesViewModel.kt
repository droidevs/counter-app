package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.R
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.usecases.preference.DisplayPreferenceUseCases
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.actions.DisplayPreferenceAction
import io.droidevs.counterapp.ui.vm.events.DisplayPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.DisplayPreferenceUiState
import io.droidevs.counterapp.ui.vm.mappers.toDisplayPreferenceUiState
import io.droidevs.counterapp.ui.vm.mappers.Quadruple
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
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
    val event: SharedFlow<DisplayPreferenceEvent> = _event.asSharedFlow()
    val uiState: StateFlow<DisplayPreferenceUiState> = combine(
        useCases.getTheme(),
        useCases.getHideControls(),
        useCases.getHideLastUpdate(),
        useCases.getKeepScreenOn(),
    ) { theme, hideControls, hideLastUpdate, keepScreenOn ->
        Quadruple(theme, hideControls, hideLastUpdate, keepScreenOn).toDisplayPreferenceUiState()
    }
        .onStart { emit(DisplayPreferenceUiState()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DisplayPreferenceUiState()
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
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(
                    message = Message.Resource(R.string.theme_updated)
                )
            )
        }
    }

    private fun setHideControls(hide: Boolean) {
        viewModelScope.launch {
            useCases.setHideControls(hide)
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(
                    message = Message.Resource(R.string.controls_visibility_updated)
                )
            )
        }
    }

    private fun setHideLastUpdate(hide: Boolean) {
        viewModelScope.launch {
            useCases.setHideLastUpdate(hide)
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(
                    message = Message.Resource(R.string.last_update_visibility_updated)
                )
            )
        }
    }

    private fun setKeepScreenOn(keep: Boolean) {
        viewModelScope.launch {
            useCases.setKeepScreenOn(keep)
            uiMessageDispatcher.dispatch(
                UiMessage.Toast(
                    message = Message.Resource(R.string.keep_screen_on_updated)
                )
            )
        }
    }
}
