package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference

class SetCounterIncrementStepUseCase(private val pref: CounterIncrementStepPreference) {
    suspend operator fun invoke(value: Int) = pref.set(value)
}

