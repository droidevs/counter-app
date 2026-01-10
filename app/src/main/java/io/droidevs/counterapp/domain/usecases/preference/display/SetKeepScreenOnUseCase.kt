package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference

class SetKeepScreenOnUseCase(private val pref: KeepScreenOnPreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

