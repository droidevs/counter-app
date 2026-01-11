package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.usecases.preference.DisplayPreferenceUseCases
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
    private val useCases: DisplayPreferenceUseCases
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
        useCases.getLabelControl()
    ) { theme, hideControls, hideLastUpdate, keepScreenOn, showLabels ->
        Quadruple(theme, hideControls, hideLastUpdate, keepScreenOn).toDisplayPreferenceUiState(showLabels)
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
            is DisplayPreferenceAction.SetShowLabels -> setShowLabels(action.show)
        }
    }

    private fun setTheme(value: Theme) {
        viewModelScope.launch {
            useCases.setTheme(value)
            _event.emit(DisplayPreferenceEvent.ShowMessage("Theme updated"))
        }
    }

    private fun setHideControls(hide: Boolean) {
        viewModelScope.launch {
            useCases.setHideControls(hide)
            _event.emit(DisplayPreferenceEvent.ShowMessage("Controls visibility updated"))
        }
    }

    private fun setHideLastUpdate(hide: Boolean) {
        viewModelScope.launch {
            useCases.setHideLastUpdate(hide)
            _event.emit(DisplayPreferenceEvent.ShowMessage("Last update visibility updated"))
        }
    }

    private fun setKeepScreenOn(keep: Boolean) {
        viewModelScope.launch {
            useCases.setKeepScreenOn(keep)
            _event.emit(DisplayPreferenceEvent.ShowMessage("Keep screen on updated"))
        }
    }

    private fun setShowLabels(show: Boolean) {
        viewModelScope.launch {
            useCases.setLabelControl(show)
            _event.emit(DisplayPreferenceEvent.ShowMessage("Labels visibility updated"))
        }
    }
}
