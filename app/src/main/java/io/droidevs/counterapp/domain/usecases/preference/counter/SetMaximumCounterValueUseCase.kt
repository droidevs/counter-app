package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference

class SetMaximumCounterValueUseCase(private val pref: MaximumCounterValuePreference) {
    suspend operator fun invoke(value: Int?) = pref.set(value)
}

