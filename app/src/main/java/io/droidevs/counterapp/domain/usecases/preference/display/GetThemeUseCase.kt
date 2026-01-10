package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.preference.display.ThemePreference
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(private val pref: ThemePreference) {
    operator fun invoke(): Flow<String> = pref.get()
}

