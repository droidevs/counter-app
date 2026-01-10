package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.preference.display.ThemePreference

class SetThemeUseCase(private val pref: ThemePreference) {
    suspend operator fun invoke(value: Theme) = pref.set(value)
}

