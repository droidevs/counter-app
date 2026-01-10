package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.preference.display.HideControlsPreference

class SetHideControlsUseCase(private val pref: HideControlsPreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

