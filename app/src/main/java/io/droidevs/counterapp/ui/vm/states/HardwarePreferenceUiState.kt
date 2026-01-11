package io.droidevs.counterapp.ui.vm.states

data class HardwarePreferenceUiState(
    val hardwareButtonControl: Boolean = false,
    val soundsOn: Boolean = true,
    val vibrationOn: Boolean = true,
    val showLabels: Boolean = true
)
