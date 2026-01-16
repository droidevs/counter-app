package io.droidevs.counterapp.preference.controle


import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DummySoundsOnPreference(
    initialValue: Boolean = true   // sounds are usually enabled by default
) : SoundsOnPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "sounds_enabled",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Result<Boolean, PreferenceError>> = delegate.flow.map { it.asSuccess() }

    override suspend fun set(value: Boolean): Result<Unit, PreferenceError> {
        delegate.set(value)
        return Unit.asSuccess()
    }

    // Optional convenience method (very common pattern)
    fun isEnabled(): Boolean = delegate.getCurrent()
}
