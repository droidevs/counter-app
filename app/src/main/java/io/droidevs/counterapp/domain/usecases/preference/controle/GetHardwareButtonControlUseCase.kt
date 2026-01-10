package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.preference.controle.HardwareButtonControlPreference
import kotlinx.coroutines.flow.Flow

class GetHardwareButtonControlUseCase(private val pref: HardwareButtonControlPreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

