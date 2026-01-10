package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import kotlinx.coroutines.flow.Flow

class GetSoundsOnUseCase(private val pref: SoundsOnPreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

