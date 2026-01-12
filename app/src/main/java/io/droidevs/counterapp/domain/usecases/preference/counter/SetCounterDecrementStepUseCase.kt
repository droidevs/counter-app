package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.CounterDecrementStepPreference

class SetCounterDecrementStepUseCase(private val pref: CounterDecrementStepPreference) {
    suspend operator fun invoke(value: Int) {
        pref.set(value)
    }
}
