package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import kotlinx.coroutines.flow.Flow

class GetKeepScreenOnUseCase(private val pref: KeepScreenOnPreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

