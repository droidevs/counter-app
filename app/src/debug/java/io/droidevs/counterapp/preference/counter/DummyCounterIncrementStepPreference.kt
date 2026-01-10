package io.droidevs.counterapp.preference.counter

// 1. CounterIncrementStepPreference
import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
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