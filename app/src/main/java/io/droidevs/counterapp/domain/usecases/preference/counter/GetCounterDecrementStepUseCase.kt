package io.droidevs.counterapp.domain.usecases.preference.counter

import io.droidevs.counterapp.domain.preference.counter.CounterDecrementStepPreference
import kotlinx.coroutines.flow.Flow

class GetCounterDecrementStepUseCase(private val pref: CounterDecrementStepPreference) {
    operator fun invoke(): Flow<Int> = pref.get()
}
