package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference

class SetHardwareButtonControlUseCase(private val pref: HardwareButtonControlPreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

