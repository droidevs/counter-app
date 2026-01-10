package io.droidevs.counterapp.data.preference.dummy.counter

// 2. DefaultCounterValuePreference
import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
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