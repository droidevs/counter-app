package io.droidevs.counterapp.ui.vm.mappers

import io.droidevs.counterapp.ui.vm.states.HardwarePreferenceUiState

@Deprecated("This mapper is no longer used and will be removed in a future version.")
fun Quadruple<Boolean, Boolean, Boolean, Boolean>.toHardwarePreferenceUiState(): HardwarePreferenceUiState {
    return HardwarePreferenceUiState(
        hardwareButtonControl = first,
        soundsOn = second,
        vibrationOn = third
    )
}
