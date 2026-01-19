package io.droidevs.counterapp.ui.vm.states

data class HardwarePreferenceUiState(
    val isLoading: Boolean = false,
    val keepScreenOn: Boolean = false,
    val hardwareButtonControl: Boolean = false,
    val soundsOn: Boolean = false,
    val vibrationOn: Boolean = false,
    val error: Boolean = false
)
