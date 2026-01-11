package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.ui.vm.states.DisplayPreferenceUiState

fun Quadruple<Theme, Boolean, Boolean, Boolean>.toDisplayPreferenceUiState(showLabels: Boolean): DisplayPreferenceUiState {
    val (theme, hideControls, hideLastUpdate, keepScreenOn) = this
    return DisplayPreferenceUiState(
        theme = theme,
        hideControls = hideControls,
        hideLastUpdate = hideLastUpdate,
        keepScreenOn = keepScreenOn,
        showLabels = showLabels
    )
}
