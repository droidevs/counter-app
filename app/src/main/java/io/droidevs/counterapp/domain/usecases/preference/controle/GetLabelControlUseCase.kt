package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference
import kotlinx.coroutines.flow.Flow

class GetLabelControlUseCase(private val pref: LabelControlPreference) {
    operator fun invoke(): Flow<Boolean> = pref.get()
}

