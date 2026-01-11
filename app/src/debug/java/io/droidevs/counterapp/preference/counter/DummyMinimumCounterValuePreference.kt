package io.droidevs.counterapp.preference.counter

// 4. MinimumCounterValuePreference (nullable Int)
import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyMinimumCounterValuePreference(
    initialValue: Int? = null   // null = no minimum
) : MinimumCounterValuePreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "counter_minimum_value",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Int?> = delegate.flow

    override suspend fun set(value: Int?) {
        delegate.set(value)
    }
}