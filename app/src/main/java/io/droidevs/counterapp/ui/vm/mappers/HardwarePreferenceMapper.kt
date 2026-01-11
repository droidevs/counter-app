package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.vm.states.HardwarePreferenceUiState

fun Quadruple<Boolean, Boolean, Boolean, Boolean>.toHardwarePreferenceUiState(): HardwarePreferenceUiState {
    return HardwarePreferenceUiState(
        hardwareButtonControl = first,
        soundsOn = second,
        vibrationOn = third,
        showLabels = fourth
    )
}
