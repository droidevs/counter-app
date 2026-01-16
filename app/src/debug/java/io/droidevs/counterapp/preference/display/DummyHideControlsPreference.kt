package io.droidevs.counterapp.preference.display

import io.droidevs.counterapp.domain.preference.display.HideControlsPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DummyHideControlsPreference(
    initialValue: Boolean = false
) : HideControlsPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "hide_controls",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Result<Boolean, PreferenceError>> = delegate.flow.map { it.asSuccess() }

    override suspend fun set(value: Boolean): Result<Unit, PreferenceError> {
        delegate.set(value)
        return Unit.asSuccess()
    }
}
