package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.usecases.preference.DisplayPreferenceUseCases
import io.droidevs.counterapp.ui.vm.actions.DisplayPreferenceAction
import io.droidevs.counterapp.ui.vm.events.DisplayPreferenceEvent
import io.droidevs.counterapp.ui.vm.states.DisplayPreferenceUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayPreferencesViewModel @Inject constructor(
    private val useCases: DisplayPreferenceUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisplayPreferenceUiState())
    val uiState: StateFlow<DisplayPreferenceUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<DisplayPreferenceEvent>()
    val event: SharedFlow<DisplayPreferenceEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            useCases.getTheme().collectLatest { theme ->
                _uiState.update { it.copy(theme = theme) }
            }
        }
        viewModelScope.launch {
            useCases.getHideControls().collectLatest { hide ->
                _uiState.update { it.copy(hideControls = hide) }
            }
        }
        viewModelScope.launch {
            useCases.getHideLastUpdate().collectLatest { hide ->
                _uiState.update { it.copy(hideLastUpdate = hide) }
            }
        }
        viewModelScope.launch {
            useCases.getKeepScreenOn().collectLatest { keep ->
                _uiState.update { it.copy(keepScreenOn = keep) }
            }
        }
        viewModelScope.launch {
            useCases.getLabelControl().collectLatest { show ->
                _uiState.update { it.copy(showLabels = show) }
            }
        }
    }

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
