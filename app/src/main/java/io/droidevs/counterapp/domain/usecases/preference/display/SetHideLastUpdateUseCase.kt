package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference

class SetHideLastUpdateUseCase(private val pref: HideLastUpdatePreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

