package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference

class SetSoundsOnUseCase(private val pref: SoundsOnPreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

