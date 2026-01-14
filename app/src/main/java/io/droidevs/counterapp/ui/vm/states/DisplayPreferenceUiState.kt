package io.droidevs.counterapp.ui.vm.states

import io.droidevs.counterapp.data.Theme

data class DisplayPreferenceUiState(
    val theme: Theme = Theme.SYSTEM,
    val hideControls: Boolean = false,
    val hideLastUpdate: Boolean = false,
    val keepScreenOn: Boolean = false
)
