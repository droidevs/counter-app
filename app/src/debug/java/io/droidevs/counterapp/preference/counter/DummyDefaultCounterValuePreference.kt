package io.droidevs.counterapp.preference.counter


import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyDefaultCounterValuePreference(
    initialValue: Int = 0
) : DefaultCounterValuePreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "default_counter_value",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Int> = delegate.flow

    override suspend fun set(value: Int) {
        delegate.set(value)
    }
}