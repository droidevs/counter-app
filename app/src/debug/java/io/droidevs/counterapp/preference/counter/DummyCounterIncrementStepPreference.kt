package io.droidevs.counterapp.preference.counter


import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyCounterIncrementStepPreference(
    initialValue: Int = 1
) : CounterIncrementStepPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "counter_increment_step",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Int> = delegate.flow

    override suspend fun set(value: Int) {
        delegate.set(value)
    }
}