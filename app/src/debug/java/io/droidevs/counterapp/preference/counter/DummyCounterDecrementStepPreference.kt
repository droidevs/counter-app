package io.droidevs.counterapp.preference.counter

import io.droidevs.counterapp.domain.preference.counter.CounterDecrementStepPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyCounterDecrementStepPreference(
    initialValue: Int = 1
) : CounterDecrementStepPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "counter_decrement_step",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Int> = delegate.flow

    override suspend fun set(value: Int) {
        delegate.set(value)
    }
}
