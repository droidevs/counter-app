package io.droidevs.counterapp.preference.counter

import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyMaximumCounterValuePreference(
    initialValue: Int? = null   // null = no maximum
) : MaximumCounterValuePreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "counter_maximum_value",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Int?> = delegate.flow

    override suspend fun set(value: Int?) {
        delegate.set(value)
    }
}