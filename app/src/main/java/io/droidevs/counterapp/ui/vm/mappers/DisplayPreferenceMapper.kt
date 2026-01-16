package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.ui.vm.states.DisplayPreferenceUiState
@Deprecated("This mapper is no longer used and will be removed in a future version.")
fun Quadruple<Theme, Boolean, Boolean, Boolean>.toDisplayPreferenceUiState(): DisplayPreferenceUiState {
    return DisplayPreferenceUiState(
        theme = first,
        hideControls = second,
        hideLastUpdate = third,
        keepScreenOn = fourth
    )
}
