package io.droidevs.counterapp.preference.counter


import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DummyDefaultCounterValuePreference(
    initialValue: Int = 0
) : DefaultCounterValuePreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "default_counter_value",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Result<Int, PreferenceError>> = delegate.flow.map { it.asSuccess() }

    override suspend fun set(value: Int): Result<Unit, PreferenceError> {
        delegate.set(value)
        return Unit.asSuccess()
    }
}
