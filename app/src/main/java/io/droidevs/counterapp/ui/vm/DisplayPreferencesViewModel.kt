package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.usecases.preference.DisplayPreferenceUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayPreferencesViewModel @Inject constructor(
    private val useCases: DisplayPreferenceUseCases
) : ViewModel() {

    val theme: StateFlow<Theme> = useCases.getTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Theme.SYSTEM
        )

    val hideControls: StateFlow<Boolean> = useCases.getHideControls()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val hideLastUpdate: StateFlow<Boolean> = useCases.getHideLastUpdate()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val keepScreenOn: StateFlow<Boolean> = useCases.getKeepScreenOn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val showLabels: StateFlow<Boolean> = useCases.getLabelControl()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    // Update methods
    fun setTheme(value: Theme) {
        viewModelScope.launch {
            useCases.setTheme(value)
        }
    }

    fun setHideControls(hide: Boolean) {
        viewModelScope.launch {
            useCases.setHideControls(hide)
        }
    }

    fun setHideLastUpdate(hide: Boolean) {
        viewModelScope.launch {
            useCases.setHideLastUpdate(hide)
        }
    }

    fun setKeepScreenOn(keep: Boolean) {
        viewModelScope.launch {
            useCases.setKeepScreenOn(keep)
        }
    }

    fun setShowLabels(show: Boolean) {
        viewModelScope.launch {
            useCases.setLabelControl(show)
        }
    }
}
