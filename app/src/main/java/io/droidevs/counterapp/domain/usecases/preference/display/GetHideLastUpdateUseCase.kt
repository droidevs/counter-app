package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.preference.display.HideLastUpdatePreference
import kotlinx.coroutines.flow.Flow

class GetHideLastUpdateUseCase(private val pref: HideLastUpdatePreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

