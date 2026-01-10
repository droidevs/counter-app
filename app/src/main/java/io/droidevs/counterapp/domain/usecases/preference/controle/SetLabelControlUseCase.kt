package io.droidevs.counterapp.domain.usecases.preference.controle

import io.droidevs.counterapp.domain.preference.controle.LabelControlPreference

class SetLabelControlUseCase(private val pref: LabelControlPreference) {
    suspend operator fun invoke(value: Boolean) = pref.set(value)
}

