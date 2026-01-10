package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference

class SetVibrationOnUseCase(private val pref: VibrationOnPreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

