package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
import kotlinx.coroutines.flow.Flow

class GetCounterIncrementStepUseCase(private val pref: CounterIncrementStepPreference) {
    operator fun invoke(): Flow<Int> = pref.get()
}

