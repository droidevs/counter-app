package io.droidevs.counterapp.domain.usecases.preference

import io.droidevs.counterapp.domain.usecases.preference.controle.GetHardwareButtonControlUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetHardwareButtonControlUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.GetSoundsOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetSoundsOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.GetVibrationOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetVibrationOnUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.GetLabelControlUseCase
import io.droidevs.counterapp.domain.usecases.preference.controle.SetLabelControlUseCase

data class HardwarePreferenceUseCases(
    val getHardwareButtonControl: GetHardwareButtonControlUseCase,
    val setHardwareButtonControl: SetHardwareButtonControlUseCase,
    val getSoundsOn: GetSoundsOnUseCase,
    val setSoundsOn: SetSoundsOnUseCase,
    val getVibrationOn: GetVibrationOnUseCase,
    val setVibrationOn: SetVibrationOnUseCase,
    val getLabelControl: GetLabelControlUseCase,
    val setLabelControl: SetLabelControlUseCase,
)
