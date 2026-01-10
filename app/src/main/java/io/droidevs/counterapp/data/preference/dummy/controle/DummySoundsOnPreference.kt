package io.droidevs.counterapp.data.preference.dummy.controle

import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.controle.SoundsOnPreference
import kotlinx.coroutines.flow.Flow

class DummySoundsOnPreference(
    initialValue: Boolean = true   // sounds are usually enabled by default
) : SoundsOnPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "sounds_enabled",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Boolean> = delegate.flow

    override suspend fun set(value: Boolean) {
        delegate.set(value)
    }

    // Optional convenience method (very common pattern)
    fun isEnabled(): Boolean = delegate.getCurrent()
}