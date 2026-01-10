package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference

class SetMinimumCounterValueUseCase(private val pref: MinimumCounterValuePreference) {
    suspend operator fun invoke(value: Int?) = pref.set(value)
}

