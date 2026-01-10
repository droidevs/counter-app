package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import kotlinx.coroutines.flow.Flow

class GetHideControlsUseCase(private val pref: HideControlsPreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

