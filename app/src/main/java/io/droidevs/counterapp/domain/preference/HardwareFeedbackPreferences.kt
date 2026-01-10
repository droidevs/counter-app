package io.droidevs.counterapp.domain.preference

import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference
import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference

data class HardwareFeedbackPreferences(
    val hardwareButtonControl: HardwareButtonControlPreference,
    val soundsOn: SoundsOnPreference,
    val vibrationOn: VibrationOnPreference
)