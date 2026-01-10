package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference

class SetDefaultCounterValueUseCase(private val pref: DefaultCounterValuePreference) {
    suspend operator fun invoke(value: Int) = pref.set(value)
}

