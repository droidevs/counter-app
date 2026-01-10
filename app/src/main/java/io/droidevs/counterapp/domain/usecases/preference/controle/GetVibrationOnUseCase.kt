package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.preference.controle.VibrationOnPreference
import kotlinx.coroutines.flow.Flow

class GetVibrationOnUseCase(private val pref: VibrationOnPreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

