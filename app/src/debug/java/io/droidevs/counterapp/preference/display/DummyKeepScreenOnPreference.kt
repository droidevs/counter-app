package io.droidevs.counterapp.preference.display


import io.droidevs.counterapp.domain.preference.display.KeepScreenOnPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyKeepScreenOnPreference(
    initialValue: Boolean = false
) : KeepScreenOnPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "keep_screen_on",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Boolean> = delegate.flow

    override suspend fun set(value: Boolean) {
        delegate.set(value)
    }
}